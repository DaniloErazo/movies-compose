package com.globant.domain.repository

import com.globant.domain.model.Movie
import com.globant.domain.model.MovieDetail

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieById(id: String): MovieDetail
    suspend fun getLocalMovies(): List<Movie>
    suspend fun saveLocalMovies(items: List<Movie>)
}