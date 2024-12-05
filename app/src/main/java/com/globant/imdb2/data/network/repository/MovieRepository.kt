package com.globant.imdb2.data.network.repository

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.globant.imdb2.data.database.dao.MovieDao
import com.globant.imdb2.data.database.entities.toDomain
import com.globant.imdb2.data.network.GetMoviesWorker
import com.globant.imdb2.data.network.model.toDomain
import com.globant.imdb2.data.network.model.toMovie
import com.globant.imdb2.data.network.services.MovieServices
import com.globant.imdb2.domain.model.Movie
import com.globant.imdb2.domain.model.MovieDetail
import com.globant.imdb2.domain.model.toDB
import com.globant.imdb2.domain.repository.MovieRepository
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MovieRepository  @Inject constructor(
    private val apiService: MovieServices,
    private val movieDao: MovieDao,
    private val workManager: WorkManager,
): MovieRepository{

    override suspend fun getPopularMovies(): List<Movie> {


        //build flavors to change variable of time repeat interval
        //save in database
        val downloadDataRequirements = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val workRequest = PeriodicWorkRequestBuilder<GetMoviesWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).setBackoffCriteria(
            BackoffPolicy.LINEAR,
            Duration.ofSeconds(30)
        ).setConstraints(downloadDataRequirements).build()
        workManager.enqueueUniquePeriodicWork("moviesWork", ExistingPeriodicWorkPolicy.KEEP, workRequest)
        return apiService.getTopMovies().body()?.results!!.map { it.toMovie() }
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