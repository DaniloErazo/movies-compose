package com.globant.imdb2.di

import android.content.Context
import androidx.room.Room
import com.globant.imdb2.database.LocalDatabase
import com.globant.imdb2.database.dao.MovieDao
import com.globant.imdb2.database.dao.UserDao
import com.globant.imdb2.services.MovieServices
import com.globant.imdb2.utils.CryptoUtils
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
    fun provideCryptoUtils(): CryptoUtils {
        return CryptoUtils() // Return a new instance of CryptoUtils
    }
}