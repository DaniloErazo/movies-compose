package com.globant.imdb2.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb2.data.database.entities.UserDB

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserDB

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserDB)
}