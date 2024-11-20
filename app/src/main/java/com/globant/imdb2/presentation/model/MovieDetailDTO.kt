package com.globant.imdb2.presentation.model

data class MovieDetailDTO (
    val identifier: String,
    val movieName: String,
    val movieImage: String,
    val score: Double,
    val backImage: String,
    val description: String,
    val genres: List<GenreDTO>
)