package com.globant.data.mappers

import com.globant.data.database.entities.MovieDB
import com.globant.data.database.entities.UserDB
import com.globant.data.network.model.GenreAPI
import com.globant.data.network.model.MovieAPI
import com.globant.data.network.model.MovieDetailAPI
import com.globant.domain.model.Genre
import com.globant.domain.model.Movie
import com.globant.domain.model.MovieDetail
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

fun MovieAPI.toDomain() : Movie {
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

fun GenreAPI.toDomain(): Genre {
    return Genre(name = this.name)
}

fun MovieDetailAPI.toDomain(): MovieDetail {
    return MovieDetail(
        identifier = this.identifier,
        movieName = this.movieName,
        movieImage = this.movieImage,
        score = this.score,
        backImage = this.backImage,
        description= this.description,
        genres = this.genres.map { it.toDomain() }
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