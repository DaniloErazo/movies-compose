package com.globant.imdb2.data.network

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.globant.imdb2.data.database.dao.MovieDao
import com.globant.imdb2.data.network.model.toDB
import com.globant.imdb2.data.network.services.MovieServices
import com.globant.imdb2.domain.model.toDB
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GetMoviesWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    @Assisted private val api: MovieServices,
    @Assisted private val movieDao: MovieDao,
    ): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val result = api.getTopMovies()
        return try {
            if (result.isSuccessful){
                movieDao.deleteAll()
                val dbMovies = result.body()?.results?.map { it.toDB()}
                if (dbMovies != null) {
                    movieDao.saveAll(dbMovies)
                }
                Result.success()
            } else {
                Result.retry()
            }
        }catch (e: Exception){
            Result.failure()
        }
    }
}