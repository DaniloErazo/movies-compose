package com.globant.imdb2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globant.imdb2.database.dao.MovieDao
import com.globant.imdb2.database.dao.UserDao
import com.globant.imdb2.database.entities.Movie
import com.globant.imdb2.database.entities.User

@Database(entities = [User::class, Movie::class], version = 1)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getMovieDao(): MovieDao
}