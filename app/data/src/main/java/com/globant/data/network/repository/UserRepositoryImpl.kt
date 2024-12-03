package com.globant.data.network.repository

import com.globant.data.database.dao.UserDao
import com.globant.data.database.entities.UserDB
import com.globant.data.mappers.toDomain
import com.globant.domain.model.User
import com.globant.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
): UserRepository {

    override suspend fun getUserByEmail(email: String): User {
        return userDao.getUserByEmail(email).toDomain()
    }

    override suspend fun addUser(email: String, name: String, hashedPassword: String, saveableSalt: String, color: Int): User{
        val newUSer = UserDB(
            email = email,
            name = name,
            password = hashedPassword,
            salt = saveableSalt,
            color = color
        )
        userDao.addUser(newUSer)
        return getUserByEmail(email)

    }
}
