package com.example.wellbeinganalytics.database

class QuizRepository (private val db: AppDatabase) {
    fun getDatabase(): AppDatabase {
        return db
    }

    suspend fun insertQuiz(quiz: Quiz) {
        db.quizDao().insertQuiz(quiz)
    }

    suspend fun getQuizById(id: Int): Quiz {
        return db.quizDao().getQuizById(id)
    }

    suspend fun getActiveQuizzes(): List<Quiz> {
        return db.quizDao().getActiveQuizzes()
    }

    suspend fun getQuestionsFromQuiz(quizId: Int): List<Question> {
        return db.quizDao().getQuestionsFromQuiz(quizId)
    }

    suspend fun getNbPerDayFromQuiz(quizId: Int): Int {
        return db.quizDao().getNbPerDayFromQuiz(quizId)
    }
}