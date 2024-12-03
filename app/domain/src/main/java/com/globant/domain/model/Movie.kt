package com.globant.domain.model

data class Movie(
    val id: String,
    val name: String,
    val image: String,
    val backImage: String,
    val score: Double,
    val date: String,
    val originalTitle: String
)