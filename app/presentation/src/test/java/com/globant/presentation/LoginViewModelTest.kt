package com.globant.presentation

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.globant.presentation.model.AuthState
import com.globant.domain.model.Response
import com.globant.domain.model.User
import com.globant.domain.usecase.LoadUserUseCase
import com.globant.domain.usecase.SignInUseCase
import com.globant.domain.usecase.SignUpUseCase
import com.globant.presentation.viewmodel.LoginViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.kotlin.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.robolectric.RobolectricTestRunner
import java.util.Base64

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var signInUseCase: SignInUseCase

    @Mock
    private lateinit var signUpUseCase: SignUpUseCase

    @Mock
    private lateinit var loadUserUseCase: LoadUserUseCase


    private val testEmail = "test@example.com"
    private val testName = "Test user"
    private val testPassword = "password"
    //private val testSaltByte = Base64.getDecoder().decode("salt".toByteArray())
    private val testSaltString = Base64.getEncoder().encodeToString("salt".toByteArray())
    private val testHashedPassword = "hashedPassword"
    private val testUser = User(
        id = 0,
        email = testEmail,
        name = testName,
        password = testHashedPassword,
        salt = testSaltString,
        color = 0
    )

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        `when`(mockContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        `when`(sharedPreferences.getBoolean("is_logged_in", false)).thenReturn(false)
        `when`(sharedPreferences.edit()).thenReturn(editor)

        viewModel = LoginViewModel(
            signUpUseCase,
            signInUseCase,
            loadUserUseCase,
            mockContext,
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `test signInUser with correct credentials`() = runTest {
        `when`(signInUseCase.invoke(testEmail, testPassword)).thenReturn(Response.Success(testUser))

        val observer = mock(Observer::class.java) as Observer<AuthState>
        viewModel.loggedUser.observeForever(observer)

        viewModel.signInUser(testEmail, testPassword)

        verify(observer).onChanged(AuthState(true, testUser))
        verify(editor).putString("username", testEmail)
        verify(editor).putBoolean("is_logged_in", true)
        verify(editor).apply()
    }

    @Test
    fun `test signInUser with incorrect password`() = runTest {
        `when`(signInUseCase.invoke(testEmail, testPassword)).thenReturn(Response.Error("Contraseña incorrecta"))

        val errorLoginObserver = mock(Observer::class.java) as Observer<String?>

        viewModel.errorLogin.observeForever(errorLoginObserver)
        viewModel.loggedUser.observeForever { }

        viewModel.signInUser(testEmail, testPassword)
        assertEquals(viewModel.loggedUser.value, null)

        verify(errorLoginObserver).onChanged("Contraseña incorrecta")
    }

    @Test
    fun `test signUpUser when email is already taken`() = runTest {
        `when`(signUpUseCase.invoke(eq(testEmail), eq(testName), eq(testPassword), any()))
            .thenReturn(Response.Error("El correo ya se encuentra registrado, por favor inicia sesión"))

        val errorLoginObserver = mock(Observer::class.java) as Observer<String?>
        viewModel.errorLogin.observeForever(errorLoginObserver)

        viewModel.signUpUser(testEmail, testName, testPassword)

        verify(errorLoginObserver).onChanged("El correo ya se encuentra registrado, por favor inicia sesión")
    }

    @Test
    fun `test signUpUser when new user signs up`() = runTest {
        `when`(signUpUseCase.invoke(eq(testEmail), eq(testName), eq(testPassword), any()))
            .thenReturn(Response.Success(testUser))
        `when`(loadUserUseCase.invoke(testEmail)).thenReturn(Response.Success(testUser))

        val loggedUserObserver = mock(Observer::class.java) as Observer<AuthState>
        val errorLoginObserver = mock(Observer::class.java) as Observer<String?>
        viewModel.loggedUser.observeForever(loggedUserObserver)

        viewModel.signUpUser(testEmail, testName, testPassword)

        verify(errorLoginObserver, never()).onChanged(any())
        verify(loggedUserObserver).onChanged(
            AuthState(
                true,
                testUser
            )
        )
    }

    @Test
    fun `test logOutCurrentUser`() = runTest {
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
        `when`(loadUserUseCase.invoke(testEmail)).thenReturn(Response.Success(testUser))

        val loggedUserObserver = mock(Observer::class.java) as Observer<AuthState>
        viewModel.loggedUser.observeForever(loggedUserObserver)

        viewModel.loadCurrentUser()

        verify(loggedUserObserver).onChanged(
            AuthState(
                true,
                testUser
            )
        )
    }

}
