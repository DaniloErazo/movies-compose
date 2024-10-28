package com.globant.imdb2.repository

import com.globant.imdb2.database.dao.MovieDao
import com.globant.imdb2.database.entities.Movie
import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.MovieResponse
import com.globant.imdb2.services.MovieServices
import retrofit2.Response
import javax.inject.Inject


class MovieRepository  @Inject constructor(private val apiService: MovieServices, private val movieDao: MovieDao){

    fun getPopularMovies(): Response<MovieResponse> {
        return apiService.getTopMovies().execute()
    }

     fun getMovieById(id: String): Response<MovieDetail>{
        return apiService.getMovieById(id = id).execute()
    }

    suspend fun getLocalMovies(): List<Movie>{
        return movieDao.getAllMovies()
    }
}