package com.globant.data.network.model

import com.globant.domain.model.Genre
import com.globant.domain.model.MovieDetail
import com.google.gson.annotations.SerializedName

data class MovieDetailAPI(
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
    val genres: List<GenreAPI>
)

fun MovieDetailAPI.toDomain(): MovieDetail {
    return MovieDetail(
        identifier = this.identifier,
        movieName = this.movieName,
        movieImage = this.movieImage,
        score = this.score,
        backImage = this.backImage,
        description= this.description,
        genres = this.genres.map { it.toDomain() }
    )
}


data class GenreAPI (
    @SerializedName("name")
    val name: String
)

fun GenreAPI.toDomain(): Genre {
    return Genre(name = this.name)
}