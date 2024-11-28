package com.globant.imdb2.domain.usecase

import com.globant.imdb2.data.exceptions.UserNotFoundException
import com.globant.imdb2.data.network.repository.DataStoreRepository
import com.globant.imdb2.data.network.repository.UserRepository
import com.globant.imdb2.domain.model.Response
import com.globant.imdb2.domain.model.User
import com.globant.imdb2.utils.CryptoUtils
import java.util.Base64
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val cryptoUtils: CryptoUtils) {

    suspend operator fun invoke(email: String, password: String): Response<User> {
        try {
            val user = userRepository.getUserByEmail(email)
            val salt = Base64.getDecoder().decode(user.salt)
            if (cryptoUtils.checkPassword(password, user.password, salt)) {
                dataStoreRepository.logInUser(user.email)
                return Response.Success(user)
            } else {
                return Response.Error("Contraseña incorrecta")
            }
        } catch (e: UserNotFoundException) {
            return Response.Error(("${e.message}"))
        }
    }
}