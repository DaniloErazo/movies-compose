package com.globant.data.network.model

import com.google.gson.annotations.SerializedName

data class MovieAPI(
    @SerializedName("id")
    val identifier: String,
    @SerializedName("original_title")
    val movieName: String,
    @SerializedName("backdrop_path")
    val backImage: String,
    @SerializedName("poster_path")
    val movieImage: String,
    @SerializedName("release_date")
    val movieDate: String,
    @SerializedName("vote_average")
    val score: Double
)

data class MovieResponse(
    val results: List<MovieAPI>
)



