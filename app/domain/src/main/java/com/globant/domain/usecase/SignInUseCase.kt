package com.globant.domain.usecase

import com.globant.domain.model.Response
import com.globant.domain.model.User
import com.globant.domain.repository.UserRepository
import com.globant.domain.utils.CryptoUtils
import java.util.Base64

class SignInUseCase (
    private val userRepository: UserRepository,
    private val cryptoUtils: CryptoUtils
) {

    suspend operator fun invoke(email: String, password: String): Response<User> {
        try {
            val user = userRepository.getUserByEmail(email)
            val salt = Base64.getDecoder().decode(user.salt)
            return if (cryptoUtils.checkPassword(password, user.password, salt)) {
                Response.Success(user)
            } else {
                Response.Error("Contraseña incorrecta")
            }
        } catch (e: Exception) {
            return Response.Error(("${e.message}"))
        }
    }
}