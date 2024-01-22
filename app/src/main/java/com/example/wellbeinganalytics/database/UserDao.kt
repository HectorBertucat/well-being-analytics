package com.example.wellbeinganalytics.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: String): User

    @Query("SELECT COUNT(*) FROM user WHERE id = :id")
    suspend fun getUserByIdCount(id: String): Int
}