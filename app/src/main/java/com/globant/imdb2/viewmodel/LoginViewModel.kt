package com.globant.imdb2.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.database.entities.User
import com.globant.imdb2.entity.AuthState
import com.globant.imdb2.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository, @ApplicationContext context: Context): ViewModel() {

    var loggedUser = MutableLiveData<AuthState>()
    var errorLogin = MutableLiveData<String>()

    private val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun signInUser(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val user = userRepository.getUserByEmail(email)

                if (user.password == password) {
                    withContext(Dispatchers.Main) {
                        loggedUser.postValue(AuthState(true, user))
                        sharedPreferences.edit().apply{
                            putString("username", user.name)
                            putBoolean("is_logged_in", true)
                        }.apply()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        errorLogin.postValue("Contraseña incorrecta")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorLogin.postValue("Problemas iniciando sesión")
                }
            }


        }
    }

    fun signUpUser(email: String, name: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val user = userRepository.getUserByEmail(email)

                if (user.email == email) {
                    withContext(Dispatchers.Main) {
                        errorLogin.postValue("El correo ya se encuentra registrado, por favor inicia sesión")
                    }
                }


            } catch (e: Exception) {
                val newUSer = User(email = email, name = name, password = password)
                userRepository.addUser(newUSer)
                withContext(Dispatchers.Main) {
                    loggedUser.postValue(AuthState(true, newUSer))
                }
            }

        }
    }
}
