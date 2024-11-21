package com.globant.imdb2.data.network.repository

import com.globant.imdb2.data.database.dao.UserDao
import com.globant.imdb2.data.database.entities.UserDB
import com.globant.imdb2.data.database.entities.toDomain
import com.globant.imdb2.domain.model.User
import com.globant.imdb2.domain.model.toDB
import com.globant.imdb2.domain.repository.UserRepository
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao): UserRepository {

    override suspend fun getUserByEmail(email: String): User {
        return userDao.getUserByEmail(email).toDomain()
    }

    override suspend fun addUser(user: User){
        return userDao.addUser(user.toDB())
    }
}
