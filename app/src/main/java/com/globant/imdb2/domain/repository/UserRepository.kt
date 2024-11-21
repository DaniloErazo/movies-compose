package com.globant.imdb2.domain.repository

import com.globant.imdb2.domain.model.User

interface UserRepository {

    suspend fun getUserByEmail(email: String): User

    suspend fun addUser(user: User)
}