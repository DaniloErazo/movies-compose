package com.globant.imdb2.domain.repository

import com.globant.imdb2.data.database.entities.UserDB

interface UserRepository {

    suspend fun getUserByEmail(email: String): UserDB

    suspend fun addUser(user: UserDB)
}