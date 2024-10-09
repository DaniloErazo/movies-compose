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

data class MovieDetail(
    @SerializedName("id")
    val identifier: String,
    @SerializedName("original_title")
    val movieName: String,
    @SerializedName("poster_path")
    val movieImage: String,
    @SerializedName("vote_average")
    val score: Double,
    @SerializedName("overview")
    val description: String,
    @SerializedName("genres")
    val genres: List<Genre>
)

data class Genre (
    val name: String
)
