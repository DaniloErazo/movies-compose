package com.globant.data.mappers

import com.globant.data.database.entities.MovieDB
import com.globant.data.database.entities.UserDB
import com.globant.domain.model.Movie
import com.globant.domain.model.User

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

fun User.toDB(): UserDB {
    return UserDB(
        id = id,
        email = email,
        name = name,
        password = password,
        salt = salt,
        color = color
    )
}