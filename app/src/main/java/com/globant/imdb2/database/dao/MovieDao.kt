package com.globant.imdb2.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.globant.imdb2.database.entities.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE identifier = :id")
    suspend fun getMovieById(id:String): Movie

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>
}