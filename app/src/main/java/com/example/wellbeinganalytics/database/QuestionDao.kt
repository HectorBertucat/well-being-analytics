package com.example.wellbeinganalytics.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestionDao {
    @Insert
    suspend fun insertQuestion(question: Question)

    @Insert
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT COUNT(*) FROM question")
    suspend fun getQuestionCount(): Int

    @Query("SELECT * FROM question WHERE quizId = :quizId")
    suspend fun getQuestionsFromQuiz(quizId: Int): List<Question>

    @Query("SELECT * FROM question WHERE id = :id")
    suspend fun getQuestionById(id: Int): Question
}