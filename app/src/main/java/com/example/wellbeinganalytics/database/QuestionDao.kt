package com.example.wellbeinganalytics.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface QuestionDao {
    @Insert
    fun insertQuestion(question: Question)

    @Insert
    fun insertQuestions(questions: List<Question>)
}