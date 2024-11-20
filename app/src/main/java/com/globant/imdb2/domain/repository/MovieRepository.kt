package com.globant.imdb2.domain.repository

import com.globant.imdb2.domain.model.Movie
import com.globant.imdb2.domain.model.MovieDetail

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieById(id: String): MovieDetail
    suspend fun getLocalMovies(): List<Movie>
    suspend fun saveLocalMovies(items: List<Movie>)
}