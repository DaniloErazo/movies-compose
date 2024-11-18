package com.globant.imdb2.repository

import com.globant.imdb2.database.dao.MovieDao
import com.globant.imdb2.database.entities.Movie
import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.MovieResponse
import com.globant.imdb2.services.MovieServices
import retrofit2.Response
import javax.inject.Inject


class MovieRepository  @Inject constructor(private val apiService: MovieServices, private val movieDao: MovieDao){

    suspend fun getPopularMovies(): MovieResponse {
        return apiService.getTopMovies()
    }

     fun getMovieById(id: String): Response<MovieDetail>{
        return apiService.getMovieById(id = id).execute()
    }

    suspend fun getLocalMovies(): List<Movie>{
        return movieDao.getAllMovies()
    }

    suspend fun saveLocalMovies(items: List<Movie>): Unit{
        movieDao.deleteAll()
        return movieDao.saveAll(items)
    }
}