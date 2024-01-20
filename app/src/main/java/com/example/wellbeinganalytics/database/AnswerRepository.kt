package com.example.wellbeinganalytics.database

class AnswerRepository (private val db: AppDatabase) {
    fun getDatabase(): AppDatabase {
        return db
    }

    suspend fun insertAnswer(answer: Answer) {
        db.answerDao().insertAnswer(answer)
    }

    suspend fun insertAnswers(answers: List<Answer>) {
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

}