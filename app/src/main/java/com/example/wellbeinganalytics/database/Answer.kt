package com.example.wellbeinganalytics.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "answer",
    foreignKeys = [
        ForeignKey(entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Quiz::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("quizId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Question::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("questionId"),
            onDelete = ForeignKey.CASCADE)
    ])

data class Answer(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val value: String,
    val userId: Int,
    val quizId: Int,
    val questionId: Int,
    val date: Date,
    val isSent: Boolean
)