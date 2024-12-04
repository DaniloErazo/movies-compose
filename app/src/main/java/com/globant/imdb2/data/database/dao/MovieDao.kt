package com.globant.imdb2.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb2.data.database.entities.MovieDB

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE identifier = :id")
    suspend fun getMovieById(id:String): MovieDB

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieDB>

    @Query("DELETE FROM movies")
    suspend fun deleteAll()

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(items: List<MovieDB>): Unit
}