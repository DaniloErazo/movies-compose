package com.globant.imdb2.di

import com.globant.imdb2.data.network.repository.DataStoreRepository
import com.globant.imdb2.data.network.repository.UserRepository
import com.globant.imdb2.domain.usecase.SignInUseCase
import com.globant.imdb2.utils.CryptoUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideSignInUseCase(
        userRepository: UserRepository,
        dataStoreRepository: DataStoreRepository,
        cryptoUtils: CryptoUtils): SignInUseCase{
        return SignInUseCase(userRepository, dataStoreRepository, cryptoUtils)
    }
}