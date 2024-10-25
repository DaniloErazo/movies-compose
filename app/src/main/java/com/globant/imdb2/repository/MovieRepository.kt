package com.globant.imdb2.repository

import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.MovieResponse
import com.globant.imdb2.services.MovieServices
import retrofit2.Response
import javax.inject.Inject


class MovieRepository  @Inject constructor(private val apiService: MovieServices){

    fun getPopularMovies(): Response<MovieResponse> {
        return apiService.getTopMovies().execute()
    }

     fun getMovieById(id: String): Response<MovieDetail>{
        return apiService.getMovieById(id = id).execute()
    }
}