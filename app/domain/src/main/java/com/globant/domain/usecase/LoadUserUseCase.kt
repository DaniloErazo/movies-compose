package com.globant.domain.usecase

import com.globant.domain.model.Response
import com.globant.domain.model.User
import com.globant.domain.repository.UserRepository

class LoadUserUseCase (private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String): Response<User> {
        return try {
            val user = userRepository.getUserByEmail(email)
            Response.Success(user)
        }catch (e: Exception){
            return Response.Error(("${e.message}"))
        }
    }
}