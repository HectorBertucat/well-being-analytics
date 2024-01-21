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
    suspend fun getNotSentAnswersFromQuizAndUser(quizId: Int, userId: String): List<Answer>

    @Query("SELECT * FROM answer WHERE userId = :userId AND isSent = 0")
    suspend fun getNotSentAnswersFromUser(userId: String): List<Answer>

    @Query("SELECT MAX(date) FROM answer WHERE userId = :userId AND quizId = :quizId")
    suspend fun getLastDateFromUserAndQuiz(userId: String, quizId: Int): String

    @Query("SELECT * FROM answer WHERE isSent = 0")
    suspend fun getNotSentAnswers(): List<Answer>

    suspend fun getNotSentQuizIds(userId: String): List<Int> {
        val answers = getNotSentAnswersFromUser(userId)
        val quizIds = mutableListOf<Int>()
        for (answer in answers) {
            if (!quizIds.contains(answer.quizId)) {
                quizIds.add(answer.quizId)
            }
        }
        return quizIds
    }
}