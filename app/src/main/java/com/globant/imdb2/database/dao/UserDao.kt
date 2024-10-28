package com.globant.imdb2.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb2.database.entities.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)
}