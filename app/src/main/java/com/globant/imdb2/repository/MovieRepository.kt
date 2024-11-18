package com.globant.imdb2.repository

import com.globant.imdb2.database.dao.MovieDao
import com.globant.imdb2.database.entities.Movie
import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.MovieResponse
import com.globant.imdb2.services.MovieServices
import javax.inject.Inject


class MovieRepository  @Inject constructor(private val apiService: MovieServices, private val movieDao: MovieDao){

    suspend fun getPopularMovies(): MovieResponse {
        return apiService.getTopMovies()
    }

    suspend fun getMovieById(id: String): MovieDetail{
        return apiService.getMovieById(id = id)
    }

    suspend fun getLocalMovies(): List<Movie>{
        return movieDao.getAllMovies()
    }

    suspend fun saveLocalMovies(items: List<Movie>){
        movieDao.deleteAll()
        return movieDao.saveAll(items)
    }
}