package com.globant.imdb2.data.network

import com.globant.imdb2.data.network.services.MovieServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val baseURL = "https://api.themoviedb.org/3/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val movieService: MovieServices by lazy { retrofit.create(MovieServices::class.java) }
}