package com.globant.imdb2.data.network

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.globant.imdb2.data.network.services.MovieServices
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GetMoviesWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    @Assisted private val api: MovieServices
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val result = api.getTopMovies()
        return try {
            if (result.isSuccessful){
                Result.success()
            } else {
                Result.retry()
            }
        }catch (e: Exception){
            Result.failure()
        }
    }
}