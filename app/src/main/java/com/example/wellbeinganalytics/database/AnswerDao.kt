package com.example.wellbeinganalytics.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnswerDao {
    @Insert
    suspend fun insertAnswer(answer: Answer)

    @Insert
    suspend fun insertAnswers(answers: List<Answer>)

    @Query("SELECT * FROM answer WHERE quizId = :quizId AND userId = :userId and isSent = 0")
    suspend fun getNotSentAnswersFromQuizAndUser(quizId: Int, userId: Int): List<Answer>

    @Query("SELECT * FROM answer WHERE userId = :userId AND isSent = 0")
    suspend fun getNotSentAnswersFromUser(userId: Int): List<Answer>

    @Query("SELECT MAX(date) FROM answer WHERE userId = :userId AND quizId = :quizId")
    suspend fun getLastDateFromUserAndQuiz(userId: Int, quizId: Int): String
}