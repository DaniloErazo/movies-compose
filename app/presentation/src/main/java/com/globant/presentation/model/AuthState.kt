package com.globant.presentation.model

import com.globant.domain.model.User

data class AuthState(
    var isLogged: Boolean = false,
    var user: User?
)