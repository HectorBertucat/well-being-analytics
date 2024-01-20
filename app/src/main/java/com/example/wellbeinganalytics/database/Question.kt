package com.example.wellbeinganalytics.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "question",
    foreignKeys = [ForeignKey(entity = Quiz::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("quizId"),
        onDelete = ForeignKey.CASCADE)])

data class Question(
    @PrimaryKey val id: Int,
    val quizId: Int,
    val content: String
)