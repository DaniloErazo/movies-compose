package com.globant.imdb2.domain.model

import com.globant.imdb2.presentation.model.MovieDetailDTO

data class MovieDetail(
    val identifier: String,
    val movieName: String,
    val movieImage: String,
    val score: Double,
    val backImage: String,
    val description: String,
    val genres: List<Genre>
)

fun MovieDetail.toDTO(): MovieDetailDTO{
    return MovieDetailDTO(
        identifier = this.identifier,
        movieName = this.movieName,
        movieImage = this.movieImage,
        score= this.score,
        backImage = this.backImage,
        description = this.description,
        genres = this.genres.map { it.toDTO() }
    )
}
