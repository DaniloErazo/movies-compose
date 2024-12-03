package com.globant.presentation.viewmodel

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.presentation.model.AuthState
import com.globant.domain.model.Response
import com.globant.domain.usecase.LoadUserUseCase
import com.globant.domain.usecase.SignInUseCase
import com.globant.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val loadUserUseCase: LoadUserUseCase,
    @ApplicationContext context: Context
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

        viewModelScope.launch {

            when (val result = signInUseCase(email, password)) {
                is Response.Success -> {
                    loggedUser.postValue(AuthState(true, result.data))
                    sharedPreferences.edit().apply {
                        putString("username", result.data!!.email)
                        putBoolean("is_logged_in", true)
                    }.apply()
                }

                is Response.Error -> {
                    errorLogin.value = result.message
                }
            }
        }
    }

    fun signUpUser(email: String, name: String, password: String) {

        viewModelScope.launch {
            val color = generateRandomColor()
            when (val result = signUpUseCase(email, name, password, color)) {
                is Response.Success -> {
                    loadUser(result.data!!.email)
                }
                is Response.Error -> {
                    errorLogin.value = result.message
                }
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

            when (val result = loadUserUseCase(email)) {
                is Response.Success -> {
                    loggedUser.postValue(AuthState(true, result.data))
                    sharedPreferences.edit().apply {
                        putString("username", result.data!!.email)
                        putBoolean("is_logged_in", true)
                    }
                }
                is Response.Error -> {
                    errorLogin.value = result.message
                }
            }
        }
    }

    fun loadCurrentUser(){
        viewModelScope.launch{
            val email = sharedPreferences.getString("username", "")

            when (val result = loadUserUseCase(email!!)) {
                is Response.Success -> {
                    loggedUser.postValue(AuthState(true, result.data))
                }
                is Response.Error -> {
                    errorLogin.value = result.message
                }
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
