package com.globant.imdb2.domain.model

import com.globant.imdb2.data.database.entities.MovieDB

data class Movie(
    val id: Int,
    val name: String,
    val image: String,
    val backImage: String,
    val score: Double,
    val date: String,
    val originalTitle: String
)

fun Movie.toDB(): MovieDB {
    return MovieDB(
        identifier = id,
        movieName = name,
        backImage = backImage,
        movieDate = date,
        movieImage = image,
        score = score
    )
}


