package com.globant.imdb2.data.network.repository

import com.globant.imdb2.data.database.dao.UserDao
import com.globant.imdb2.data.database.entities.UserDB
import com.globant.imdb2.domain.repository.UserRepository
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao): UserRepository {

    override suspend fun getUserByEmail(email: String): UserDB {
        return userDao.getUserByEmail(email)
    }

    override suspend fun addUser(user: UserDB){
        return userDao.addUser(user)
    }
}
