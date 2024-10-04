package com.globant.imdb2.model

data class Movie(
    val id: String,
    val name: String,
    val image: String,
    val score: Double,
    val originalTitle: String,
    val description: String,
)

