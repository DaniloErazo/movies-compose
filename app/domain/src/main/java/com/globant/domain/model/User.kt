package com.globant.domain.model


data class User(
    val id: Int,
    val email: String,
    val name: String,
    val password: String,
    val salt: String,
    val color: Int
)


