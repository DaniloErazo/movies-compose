package com.globant.domain.repository

import com.globant.domain.model.User

interface UserRepository {

    suspend fun getUserByEmail(email: String): User

    suspend fun addUser(user: User)
}