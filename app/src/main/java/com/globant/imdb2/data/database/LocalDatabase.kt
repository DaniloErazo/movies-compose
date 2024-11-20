package com.globant.imdb2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globant.imdb2.data.database.dao.MovieDao
import com.globant.imdb2.data.database.dao.UserDao
import com.globant.imdb2.data.database.entities.MovieDB
import com.globant.imdb2.data.database.entities.UserDB

@Database(entities = [UserDB::class, MovieDB::class], version = 1)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getMovieDao(): MovieDao
}