package com.globant.imdb2

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.globant.imdb2.presentation.model.AuthState
import com.globant.imdb2.data.network.repository.UserRepository
import com.globant.imdb2.domain.model.User
import com.globant.imdb2.utils.CryptoUtils
import com.globant.imdb2.presentation.viewmodel.LoginViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.robolectric.RobolectricTestRunner
import java.util.Base64

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var cryptoUtils: CryptoUtils


    private val testEmail = "test@example.com"
    private val testName = "Test user"
    private val testPassword = "password"
    private val testSaltByte = Base64.getDecoder().decode("salt".toByteArray())
    private val testSaltString = Base64.getEncoder().encodeToString("salt".toByteArray())
    private val testHashedPassword = "hashedPassword"
    private val testUser = User(id= 0, email = testEmail, name = testName, password = testHashedPassword, salt = testSaltString, color = 0)

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        `when`(mockContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        `when`(sharedPreferences.getBoolean("is_logged_in", false)).thenReturn(false)
        `when`(sharedPreferences.edit()).thenReturn(editor)

        viewModel = LoginViewModel(userRepository, mockContext, cryptoUtils)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test signInUser with correct credentials`() = runBlocking {
        `when`(userRepository.getUserByEmail(testEmail)).thenReturn(testUser)
        `when`(cryptoUtils.checkPassword(any(), any(), any())).thenReturn(true)

        val observer = mock(Observer::class.java) as Observer<AuthState>
        viewModel.loggedUser.observeForever(observer)

        viewModel.signInUser(testEmail, testPassword)

        verify(observer).onChanged(AuthState(true, testUser))
        verify(editor).putString("username", testEmail)
        verify(editor).putBoolean("is_logged_in", true)
        verify(editor).apply()
    }

    @Test
    fun `test signInUser with incorrect password`() = runBlocking {
        `when`(userRepository.getUserByEmail(testEmail)).thenReturn(testUser)
        `when`(cryptoUtils.checkPassword(testPassword, testHashedPassword, testSaltByte)).thenReturn(false)

        val errorLoginObserver = mock(Observer::class.java) as Observer<String>

        viewModel.errorLogin.observeForever(errorLoginObserver)
        viewModel.loggedUser.observeForever { }

        viewModel.signInUser(testEmail, testPassword)
        assertEquals(viewModel.loggedUser.value, null)

        verify(errorLoginObserver).onChanged("Contraseña incorrecta")
    }

    @Test
    fun `test signUpUser when email is already taken`() = runBlocking {
        `when`(userRepository.getUserByEmail(testEmail)).thenReturn(testUser)
        `when`(cryptoUtils.checkPassword(any(), any(), any())).thenReturn(false)

        val errorLoginObserver = mock(Observer::class.java) as Observer<String>
        viewModel.errorLogin.observeForever(errorLoginObserver)

        viewModel.signUpUser(testEmail, "Test User", testPassword)

        verify(errorLoginObserver).onChanged("El correo ya se encuentra registrado, por favor inicia sesión")
    }

    @Test
    fun `test signUpUser when new user signs up`() = runBlocking {
        `when`(userRepository.getUserByEmail(testEmail)).thenThrow(RuntimeException())

        `when`(cryptoUtils.generateSalt()).thenReturn(testSaltByte)
        `when`(cryptoUtils.hashPassword(any(), any())).thenReturn(testHashedPassword)

        val loggedUserObserver = mock(Observer::class.java) as Observer<AuthState>
        viewModel.loggedUser.observeForever(loggedUserObserver)

        viewModel.signUpUser(testEmail, "Test User", testPassword)

        verify(loggedUserObserver).onChanged(argThat {
            // Check that the AuthState is not null and contains the correct user
            this.isLogged && this.user?.email == testEmail
        })
    }

    @Test
    fun `test logOutCurrentUser`() = runBlocking {
        val loggedUserObserver = mock(Observer::class.java) as Observer<AuthState>
        viewModel.loggedUser.observeForever(loggedUserObserver)

        viewModel.logOutCurrentUser()

        verify(loggedUserObserver).onChanged(AuthState(false, null))

        verify(editor).putString("username", "")
        verify(editor).putBoolean("is_logged_in", false)
        verify(editor).apply()
    }

    @Test
    fun `test loadCurrentUser when user is logged in`() = runBlocking {
        `when`(sharedPreferences.getString("username", "")).thenReturn(testEmail)
        `when`(userRepository.getUserByEmail(testEmail)).thenReturn(testUser)

        val loggedUserObserver = mock(Observer::class.java) as Observer<AuthState>
        viewModel.loggedUser.observeForever(loggedUserObserver)

        viewModel.loadCurrentUser()

        verify(loggedUserObserver).onChanged(AuthState(true, testUser))
    }

}
