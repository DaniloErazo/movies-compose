package com.globant.imdb2.entity

import com.globant.imdb2.database.entities.User

data class AuthState(
    var isLogged: Boolean = false,
    var user: User
)