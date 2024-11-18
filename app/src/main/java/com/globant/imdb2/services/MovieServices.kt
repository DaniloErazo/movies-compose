package com.globant.imdb2.services

import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieServices {

    @GET("movie/popular")
    suspend fun getTopMovies(@Query("api_key") apiKey: String = "749058a6469a1eb756bd200fa7ebb58e"): MovieResponse

    @GET("movie/{id}")
    suspend fun getMovieById(@Path("id") id:String, @Query("api_key") apiKey: String = "749058a6469a1eb756bd200fa7ebb58e"): MovieDetail

    @GET("movie/top_rated")
    fun getBestMovies(@Query("api_key") apiKey: String = "749058a6469a1eb756bd200fa7ebb58e"): Call<MovieResponse>

}