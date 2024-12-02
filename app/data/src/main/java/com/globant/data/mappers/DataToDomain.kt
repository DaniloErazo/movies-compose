package com.globant.data.mappers

import com.globant.data.database.entities.MovieDB
import com.globant.data.database.entities.UserDB
import com.globant.domain.model.Movie
import com.globant.domain.model.User

fun MovieDB.toDomain() : Movie {
    return Movie(
        id = identifier,
        name = movieName,
        image = movieImage,
        backImage = backImage,
        score = score,
        date = movieDate,
        originalTitle = movieName
    )
}

fun UserDB.toDomain() : User {
    return User(
        id = id,
        email = email,
        name = name,
        password = password,
        salt = salt,
        color = color
    )
}