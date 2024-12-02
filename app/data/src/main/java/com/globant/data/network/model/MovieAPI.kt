package com.globant.data.network.model

import com.globant.domain.model.Movie
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

fun MovieAPI.toDB(): com.globant.data.database.entities.MovieDB {
    return com.globant.data.database.entities.MovieDB(
        identifier = this.identifier,
        movieName = this.movieName,
        backImage = this.backImage,
        movieImage = this.movieImage,
        movieDate = this.movieDate,
        score = this.score
    )
}

fun MovieAPI.toMovie(): Movie {
    return Movie(
        id = this.identifier,
        name = this.movieName,
        image = this.movieImage,
        originalTitle = this.movieName,
        date = this.movieDate,
        score = this.score,
        backImage = this.backImage
    )
}


data class MovieResponse(
    val results: List<MovieAPI>
)



