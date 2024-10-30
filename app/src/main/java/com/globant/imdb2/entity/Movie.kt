package com.globant.imdb2.entity

import com.globant.imdb2.database.entities.Movie
import com.google.gson.annotations.SerializedName

data class MovieDTO(
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

fun MovieDTO.toEntity(): Movie {
    return Movie(
        identifier = this.identifier,
        movieName = this.movieName,
        backImage = this.backImage,
        movieImage = this.movieImage,
        movieDate = this.movieDate,
        score = this.score
    )
}

fun Movie.toMovieDTO(): MovieDTO {
    return MovieDTO(
        identifier = this.identifier,
        movieName = this.movieName,
        backImage = this.backImage,
        movieImage = this.movieImage,
        movieDate = this.movieDate,
        score = this.score
    )
}



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
    @SerializedName("backdrop_path")
    val backImage: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("genres")
    val genres: List<Genre>
)

data class Genre (
    @SerializedName("name")
    val name: String
)
