package com.globant.imdb2.data.database.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.globant.imdb2.domain.model.User

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
)

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
