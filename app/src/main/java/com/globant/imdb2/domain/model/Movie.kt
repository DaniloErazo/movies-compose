package com.globant.imdb2.domain.model

import com.globant.imdb2.data.database.entities.MovieDB
import com.globant.imdb2.presentation.model.MovieDTO

data class Movie(
    val id: String,
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

fun Movie.toDTO(): MovieDTO {
    return MovieDTO(
        identifier = this.id,
        movieName = this.name,
        backImage = this.backImage,
        movieImage = this.image,
        movieDate = this.date,
        score = this.score
    )
}

