package com.example.wellbeinganalytics.database

import android.util.Log

class AnswerRepository (private val db: AppDatabase) {
    fun getDatabase(): AppDatabase {
        return db
    }

    suspend fun insertAnswer(answer: Answer) {
        val user = db.userDao().getUserById(answer.userId)
        val quiz = db.quizDao().getQuizById(answer.quizId)
        val question = db.questionDao().getQuestionById(answer.questionId)

        Log.d("InsertAnswer", "User exists: $user")
        Log.d("InsertAnswer", "Quiz exists: $quiz")
        Log.d("InsertAnswer", "Question exists: $question")
        db.answerDao().insertAnswer(answer)
    }

    suspend fun insertAnswers(answers: List<Answer>) {
        for (answer in answers) {
            val user = db.userDao().getUserById(answer.userId)
            val quiz = db.quizDao().getQuizById(answer.quizId)
            val question = db.questionDao().getQuestionById(answer.questionId)

            Log.d("InsertAnswer", "User exists: $user")
            Log.d("InsertAnswer", "Quiz exists: $quiz")
            Log.d("InsertAnswer", "Question exists: $question")
        }

        db.answerDao().insertAnswers(answers)
    }

    suspend fun getNotSentAnswersFromQuizAndUser(quizId: Int, userId: String): List<Answer> {
        return db.answerDao().getNotSentAnswersFromQuizAndUser(quizId, userId)
    }

    suspend fun getNotSentAnswersFromUser(userId: String): List<Answer> {
        return db.answerDao().getNotSentAnswersFromUser(userId)
    }

    suspend fun getLastDateFromUserAndQuiz(userId: String, quizId: Int): String {
        return db.answerDao().getLastDateFromUserAndQuiz(userId, quizId)
    }

    suspend fun getNotSentAnswers(): List<Answer> {
        return db.answerDao().getNotSentAnswers()
    }

}