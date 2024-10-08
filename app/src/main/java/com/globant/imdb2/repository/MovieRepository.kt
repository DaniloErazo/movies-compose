package com.globant.imdb2.repository

import com.globant.imdb2.entity.MovieResponse
import com.globant.imdb2.network.RetrofitInstance
import retrofit2.Response

class MovieRepository {

    private val movieService = RetrofitInstance.movieService

    fun getPopularMovies(): Response<MovieResponse> {
        return movieService.getTopMovies().execute()
    }
}