package com.example.wellbeinganalytics.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val active: Boolean
)
