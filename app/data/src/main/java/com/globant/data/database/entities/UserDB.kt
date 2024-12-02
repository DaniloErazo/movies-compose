package com.globant.data.database.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name= "password")
    val password: String,
    @ColumnInfo(name = "salt")
    val salt: String,
    @ColumnInfo(name = "avatar_color")
    val color: Int
) {
}

