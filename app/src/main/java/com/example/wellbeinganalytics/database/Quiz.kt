package com.example.wellbeinganalytics.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class Quiz(
    @PrimaryKey val id: Int,
    val name: String,
    val nbPerDay: Int,
    val active: Boolean
)
