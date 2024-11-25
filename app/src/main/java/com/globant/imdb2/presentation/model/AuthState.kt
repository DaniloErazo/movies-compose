package com.globant.imdb2.presentation.model

import com.globant.imdb2.domain.model.User

data class AuthState(
    var isLogged: Boolean = false,
    var user: User?
)