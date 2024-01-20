package com.example.wellbeinganalytics.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
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
    ]
)

data class Answer(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val value: Int,
    val userId: String,
    val quizId: Int,
    val questionId: Int,
    val date: Date?,
    val isSent: Boolean = false
) {
    constructor(
        value: Int,
        userId: String,
        quizId: Int,
        questionId: Int,
        date: Date?,
        isSent: Boolean = false
    ) : this(null, value, userId, quizId, questionId, date, isSent)
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}