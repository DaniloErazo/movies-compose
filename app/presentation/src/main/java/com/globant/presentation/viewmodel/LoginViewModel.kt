package com.globant.presentation.viewmodel

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.repository.UserRepository
import com.globant.presentation.model.AuthState
import com.globant.data.utils.CryptoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import java.util.Base64
import kotlin.random.Random

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext context: Context,
    private val cryptoUtils: CryptoUtils
): ViewModel() {

    var loggedUser = MutableLiveData<AuthState>()
    var errorLogin = MutableLiveData<String?>()

    private val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun clearError(){
        errorLogin.value = null
    }

    fun signInUser(email: String, password: String) {

        viewModelScope.launch{

            try {
                val user = userRepository.getUserByEmail(email)
                val salt = Base64.getDecoder().decode(user.salt)
                if(cryptoUtils.checkPassword(password, user.password, salt)){
                    loggedUser.postValue(AuthState(true, user))
                    sharedPreferences.edit().apply{
                        putString("username", user.email)
                        putBoolean("is_logged_in", true)
                    }.apply()
                } else {
                    errorLogin.postValue("Contraseña incorrecta")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println(e)
                    errorLogin.postValue("Problemas iniciando sesión")
                }
            }


        }
    }

    fun signUpUser(email: String, name: String, password: String) {

        viewModelScope.launch{

            try {
                val user = userRepository.getUserByEmail(email)

                if (user.email == email) {
                    withContext(Dispatchers.Main) {
                        errorLogin.postValue("El correo ya se encuentra registrado, por favor inicia sesión")
                    }
                }

            } catch (e: Exception) {

                val color = generateRandomColor()
                userRepository.addUser(email, name, password, color)
                loadUser(email)
            }

        }
    }

    fun logOutCurrentUser(){
        viewModelScope.launch{
            loggedUser.postValue(AuthState(false, null))
            sharedPreferences.edit().apply{
                putString("username", "")
                putBoolean("is_logged_in", false)
            }.apply()
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
                val email = sharedPreferences.getString("username", "")
                val user = userRepository.getUserByEmail(email!!)
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