package com.globant.imdb2.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.globant.imdb2.domain.model.Movie

@Entity(tableName = "movies")
data class MovieDB (
    @PrimaryKey
    val identifier: String,
    @ColumnInfo(name = "name")
    val movieName: String,
    @ColumnInfo(name = "release_date")
    val movieDate: String,
    @ColumnInfo(name = "image_path")
    val movieImage: String,
    @ColumnInfo(name = "background_image_path")
    val backImage: String,
    @ColumnInfo(name = "score")
    val score: Double
)

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
