package com.globant.imdb2.entity

import com.google.gson.annotations.SerializedName

data class MovieDTO(
    @SerializedName("id")
    val identifier: String,
    @SerializedName("original_title")
    val movieName: String,
    @SerializedName("poster_path")
    val movieImage: String,
    @SerializedName("vote_average")
    val score: Double
)

data class MovieResponse(
    val results: List<MovieDTO>
)
