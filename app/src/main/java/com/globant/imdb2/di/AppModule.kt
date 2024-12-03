package com.globant.imdb2.di

import android.content.Context
import androidx.room.Room
import com.globant.data.database.LocalDatabase
import com.globant.data.database.dao.MovieDao
import com.globant.data.database.dao.UserDao
import com.globant.data.network.repository.MovieRepositoryImpl
import com.globant.data.network.repository.UserRepositoryImpl
import com.globant.data.network.services.MovieServices
import com.globant.domain.repository.MovieRepository
import com.globant.domain.repository.UserRepository
import com.globant.domain.usecase.LoadUserUseCase
import com.globant.domain.usecase.SignInUseCase
import com.globant.domain.usecase.SignUpUseCase
import com.globant.domain.utils.CryptoUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private const val DATABASE_NAME = "local_database"

    @Provides
    //@Singleton review
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieServices {
        return retrofit.create(MovieServices::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: LocalDatabase): UserDao {
        return db.getUserDao()
    }

    @Provides
    @Singleton
    fun provideMovieDao(db: LocalDatabase): MovieDao {
        return db.getMovieDao()

    }

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    fun provideMovieRepository(movieDao: MovieDao, movieServices: MovieServices): MovieRepository {
        return MovieRepositoryImpl(movieServices, movieDao)
    }

    @Provides
    fun provideSignInUseCase(userRepository: UserRepository, cryptoUtils: CryptoUtils): SignInUseCase{
        return SignInUseCase(userRepository, cryptoUtils)
    }

    @Provides
    fun provideSignUpUseCase(userRepository: UserRepository, cryptoUtils: CryptoUtils): SignUpUseCase{
        return SignUpUseCase(userRepository, cryptoUtils)
    }

    @Provides
    fun provideLoadUserUseCase(userRepository: UserRepository): LoadUserUseCase {
        return LoadUserUseCase(userRepository)
    }

    @Provides
    fun provideCryptoUtils(): CryptoUtils {
        return CryptoUtils()
    }
}