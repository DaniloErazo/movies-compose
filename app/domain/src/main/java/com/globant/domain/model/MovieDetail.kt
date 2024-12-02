package com.globant.domain.model

data class MovieDetail(
    val identifier: String,
    val movieName: String,
    val movieImage: String,
    val score: Double,
    val backImage: String,
    val description: String,
    val genres: List<Genre>
)
