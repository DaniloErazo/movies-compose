package com.globant.imdb2.presentation.model

data class MovieDTO(
    val identifier: String,
    val movieName: String,
    val backImage: String,
    val movieImage: String,
    val movieDate: String,
    val score: Double
)

