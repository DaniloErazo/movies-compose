package com.globant.imdb2.services

import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.entity.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieServices {

    @GET("movie/popular")
    fun getTopMovies(@Query("api_key") apiKey: String = "749058a6469a1eb756bd200fa7ebb58e"): Call<MovieResponse>

    @GET("movie/{id]")
    fun getMovieById(@Query("api_key") apiKey: String = "749058a6469a1eb756bd200fa7ebb58e", @Path("id") id:String): MovieDTO

}