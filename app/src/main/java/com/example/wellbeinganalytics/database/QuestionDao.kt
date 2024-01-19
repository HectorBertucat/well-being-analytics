package com.example.wellbeinganalytics.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface QuestionDao {
    @Insert
    suspend fun insertQuestion(question: Question)

    @Insert
    suspend fun insertQuestions(questions: List<Question>)
}