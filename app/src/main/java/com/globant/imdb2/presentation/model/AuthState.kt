package com.globant.imdb2.presentation.model

data class AuthState(
    var isLogged: Boolean = false,
    var user: UserDTO?
)