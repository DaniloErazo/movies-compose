package com.globant.imdb2.domain.model

import com.globant.imdb2.data.database.entities.UserDB

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val password: String,
    val salt: String,
    val color: Int
)

fun User.toDB(): UserDB {
    return UserDB(
        id = id,
        email = email,
        name = name,
        password = password,
        salt = salt,
        color = color
    )
}
