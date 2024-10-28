package com.globant.imdb2.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie (
    @PrimaryKey
    val identifier: String,
    @ColumnInfo(name = "name")
    val movieName: String,
    @ColumnInfo(name = "release_date")
    val movieDate: String,
    @ColumnInfo(name = "image_path")
    val movieImage: String
)
