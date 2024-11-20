package com.globant.imdb2.data.network.model

import com.globant.imdb2.data.database.entities.MovieDB
import com.globant.imdb2.domain.model.Genre
import com.globant.imdb2.domain.model.Movie
import com.globant.imdb2.domain.model.MovieDetail
import com.globant.imdb2.presentation.model.MovieDTO
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

fun MovieAPI.toDB(): MovieDB {
    return MovieDB(
        identifier = this.identifier,
        movieName = this.movieName,
        backImage = this.backImage,
        movieImage = this.movieImage,
        movieDate = this.movieDate,
        score = this.score
    )
}

fun MovieAPI.toDTO(): MovieDTO{
    return MovieDTO(
        identifier = this.identifier,
        movieName = this.movieName,
        backImage = this.backImage,
        movieImage = this.movieImage,
        movieDate = this.movieDate,
        score = this.score
    )
}

fun MovieAPI.toMovie(): Movie{
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



