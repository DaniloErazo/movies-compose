package com.globant.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globant.data.database.dao.MovieDao
import com.globant.data.database.dao.UserDao
import com.globant.data.database.entities.MovieDB
import com.globant.data.database.entities.UserDB

@Database(entities = [UserDB::class, MovieDB::class], version = 1)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getMovieDao(): MovieDao
}