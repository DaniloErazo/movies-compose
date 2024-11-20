package com.globant.imdb2.data.network.repository

import com.globant.imdb2.data.database.dao.MovieDao
import com.globant.imdb2.data.database.entities.toDomain
import com.globant.imdb2.data.network.model.toDomain
import com.globant.imdb2.data.network.model.toMovie
import com.globant.imdb2.data.network.services.MovieServices
import com.globant.imdb2.domain.model.Movie
import com.globant.imdb2.domain.model.MovieDetail
import com.globant.imdb2.domain.model.toDB
import com.globant.imdb2.domain.repository.MovieRepository
import javax.inject.Inject


class MovieRepository  @Inject constructor(private val apiService: MovieServices, private val movieDao: MovieDao): MovieRepository{

    override suspend fun getPopularMovies(): List<Movie> {
        return apiService.getTopMovies().results.map { it.toMovie() }
    }

    override suspend fun getMovieById(id: String): MovieDetail {
        return apiService.getMovieById(id = id).toDomain()
    }

    override suspend fun getLocalMovies(): List<Movie> {
        return movieDao.getAllMovies().map { it.toDomain() }
    }

    override suspend fun saveLocalMovies(items: List<Movie>){
        movieDao.deleteAll()
        val dbMovies = items.map { it.toDB()}
        return movieDao.saveAll(dbMovies)
    }
}