package com.globant.imdb2.domain.model

import com.globant.imdb2.data.database.entities.UserDB
import com.globant.imdb2.presentation.model.UserDTO

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val password: String,
    val salt: String,
    val color: Int
)

fun User.toDTO(): UserDTO {
    return UserDTO(
        id = this.id,
        email = this.email,
        name = this.name,
        color = this.color
    )
}

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
