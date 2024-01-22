package com.example.wellbeinganalytics.database

class AnswerRepository (private val db: AppDatabase) {
    fun getDatabase(): AppDatabase {
        return db
    }

    suspend fun insertAnswer(answer: Answer) {
        val user = db.userDao().getUserById(answer.userId)
        val quiz = db.quizDao().getQuizById(answer.quizId)
        val question = db.questionDao().getQuestionById(answer.questionId)

        db.answerDao().insertAnswer(answer)
    }

    suspend fun getAnswer(id: Int): Answer {
        return db.answerDao().getAnswer(id)
    }

    suspend fun setAnswerSent(id: Int) {
        db.answerDao().setAnswerSent(id)
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

    suspend fun getNotSentAnswers(): List<Answer> {
        return db.answerDao().getNotSentAnswers()
    }

    suspend fun getNotSentQuizIds(userId: String): List<Int> {
        return db.answerDao().getNotSentQuizIds(userId)
    }

}