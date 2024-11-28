package com.globant.imdb2.presentation.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.data.database.entities.UserDB
import com.globant.imdb2.data.database.entities.toDomain
import com.globant.imdb2.data.network.repository.DataStoreRepository
import com.globant.imdb2.presentation.model.AuthState
import com.globant.imdb2.data.network.repository.UserRepository
import com.globant.imdb2.domain.model.Response
import com.globant.imdb2.domain.usecase.SignInUseCase
import com.globant.imdb2.utils.CryptoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import java.util.Base64
import kotlin.random.Random

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val signInUseCase: SignInUseCase,
    private val cryptoUtils: CryptoUtils): ViewModel() {

    var loggedUser = MutableLiveData<AuthState>()
    var errorLogin = MutableStateFlow<String?>("")

    fun isLoggedIn(): Boolean {
        return dataStoreRepository.isLogged()
    }

    fun signInUser(email: String, password: String) {

        viewModelScope.launch{

            when(val result = signInUseCase(email, password)){
                is Response.Success -> {
                    loggedUser.postValue(AuthState(true, result.data))
                }
                is Response.Error -> {
                    errorLogin.value = result.message
                }
            }

        }
    }

    fun clearError(){
        errorLogin.value = null
    }

    fun signUpUser(email: String, name: String, password: String) {

        viewModelScope.launch{

            try {
                val user = userRepository.getUserByEmail(email)

                if (user.email == email) {
                    withContext(Dispatchers.Main) {
                        errorLogin.value = ("El correo ya se encuentra registrado, por favor inicia sesión")
                    }
                }

            } catch (e: Exception) {
                val salt = cryptoUtils.generateSalt()
                val hashedPassword = cryptoUtils.hashPassword(password, salt)
                val saveableSalt = Base64.getEncoder().encodeToString(salt)
                val avatarColor = generateRandomColor()
                val newUSer = UserDB(
                    email = email,
                    name = name,
                    password = hashedPassword,
                    salt = saveableSalt,
                    color = avatarColor)

                userRepository.addUser(newUSer.toDomain())
                loggedUser.postValue(AuthState(true, newUSer.toDomain()))

            }

        }
    }

    fun logOutCurrentUser(){
        viewModelScope.launch{
            loggedUser.postValue(AuthState(false, null))
            dataStoreRepository.logOutUser()
        }
    }

    fun loadUser(email: String){
        viewModelScope.launch{
            try {
                val user = userRepository.getUserByEmail(email)
                loggedUser.postValue(AuthState(true, user))
            }catch (e: Exception){
                //
            }
        }
    }

    fun loadCurrentUser(){
        viewModelScope.launch{
            try {
                val email = dataStoreRepository.getLoggedUser()
                val user = userRepository.getUserByEmail(email)
                loggedUser.postValue(AuthState(true, user))
            }catch (e: Exception){
                //
            }
        }
    }

    private fun generateRandomColor(): Int {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)

        return Color.argb(255, red, green, blue)
    }
}
