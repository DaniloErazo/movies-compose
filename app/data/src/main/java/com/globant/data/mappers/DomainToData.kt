package com.globant.data.mappers

import com.globant.data.database.entities.MovieDB
import com.globant.domain.model.Movie

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
