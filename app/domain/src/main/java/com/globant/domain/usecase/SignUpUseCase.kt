package com.globant.domain.usecase

import com.globant.domain.model.Response
import com.globant.domain.model.User
import com.globant.domain.repository.UserRepository
import com.globant.domain.utils.CryptoUtils
import java.util.Base64

class SignUpUseCase(
    private val userRepository: UserRepository,
    private val cryptoUtils: CryptoUtils
) {

    suspend operator fun invoke(email: String, name: String, password: String, color: Int): Response<User> {
        return try {
            userRepository.getUserByEmail(email)
            Response.Error("El correo ya se encuentra registrado, por favor inicia sesión")
        } catch (e: Exception) {
            val salt = cryptoUtils.generateSalt()
            val hashedPassword = cryptoUtils.hashPassword(password, salt)
            val saveableSalt = Base64.getEncoder().encodeToString(salt)
            val user = userRepository.addUser(email, name, hashedPassword, saveableSalt, color)
            Response.Success(user)
        }
    }
}