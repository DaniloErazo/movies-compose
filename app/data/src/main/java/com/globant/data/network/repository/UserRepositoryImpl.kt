package com.globant.data.network.repository

import com.globant.data.database.dao.UserDao
import com.globant.data.database.entities.UserDB
import com.globant.data.mappers.toDB
import com.globant.data.mappers.toDomain
import com.globant.data.utils.CryptoUtils
import com.globant.domain.model.User
import com.globant.domain.repository.UserRepository
import java.util.Base64
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val cryptoUtils: CryptoUtils
): UserRepository {

    override suspend fun getUserByEmail(email: String): User {
        return userDao.getUserByEmail(email).toDomain()
    }

    override suspend fun addUser(email: String, name: String, password: String, color: Int){
        val salt = cryptoUtils.generateSalt()
        val hashedPassword = cryptoUtils.hashPassword(password, salt)
        val saveableSalt = Base64.getEncoder().encodeToString(salt)
        val newUSer = UserDB(
            email = email,
            name = name,
            password = hashedPassword,
            salt = saveableSalt,
            color = color
        )
        return userDao.addUser(newUSer)

    }
}
