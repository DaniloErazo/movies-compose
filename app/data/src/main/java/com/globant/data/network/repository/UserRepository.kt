package com.globant.data.network.repository

import com.globant.data.database.dao.UserDao
import com.globant.data.mappers.toDB
import com.globant.data.mappers.toDomain
import com.globant.domain.model.User
import com.globant.domain.repository.UserRepository
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao): UserRepository {

    override suspend fun getUserByEmail(email: String): User {
        return userDao.getUserByEmail(email).toDomain()
    }

    override suspend fun addUser(user: User){
        return userDao.addUser(user.toDB())
    }
}
