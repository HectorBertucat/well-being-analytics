package com.example.wellbeinganalytics.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuizDao {
    @Insert
    suspend fun insertQuiz(quiz: Quiz)

    @Query("SELECT * FROM quiz WHERE id = :id")
    suspend fun getQuizById(id: Int): Quiz

    @Query("SELECT * FROM quiz WHERE active = 1")
    suspend fun getActiveQuizzes(): List<Quiz>

    @Query("SELECT * FROM question WHERE quizId = :quizId")
    suspend fun getQuestionsFromQuiz(quizId: Int): List<Question>
}
