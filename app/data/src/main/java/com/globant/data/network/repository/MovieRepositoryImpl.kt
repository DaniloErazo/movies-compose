package com.globant.data.network.repository

import com.globant.data.database.dao.MovieDao
import com.globant.data.mappers.toDB
import com.globant.data.mappers.toDomain
import com.globant.data.network.model.toDomain
import com.globant.data.network.services.MovieServices
import com.globant.domain.model.Movie
import com.globant.domain.model.MovieDetail
import com.globant.domain.repository.MovieRepository
import javax.inject.Inject


class MovieRepositoryImpl  @Inject constructor(private val apiService: MovieServices, private val movieDao: MovieDao): MovieRepository{

    override suspend fun getPopularMovies(): List<Movie> {
        return apiService.getTopMovies().results.map { it.toDomain() }
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