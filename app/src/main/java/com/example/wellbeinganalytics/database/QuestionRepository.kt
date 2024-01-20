package com.example.wellbeinganalytics.database

class QuestionRepository(private val db: AppDatabase) {
    private val questions = listOf(
        Question(1, 1, "I slept very well and feel that my sleep was totally restorative."),
        Question(2, 1, "I feel totally rested after this night's sleep."),
        Question(3, 2, "I related easily to the people around me."),
        Question(4, 2, "I was able to face difficult situations in a positive way."),
        Question(5, 2, "I felt that others liked me and appreciated me."),
        Question(6, 2, "I felt satisfied with what I was able to achieve, I felt proud of myself."),
        Question(7, 2, "My life was well balanced between my family, personal and academic activities.."),
        Question(8, 3, "I felt emotionally balanced."),
        Question(9, 3, "I felt good, at peace with myself."),
        Question(10, 3, "I felt confident.")
    )

    fun getDatabase(): AppDatabase {
        return db
    }

    suspend fun insertQuestions() {
        if (db.questionDao().getQuestionCount() == 0) {
            if (db.quizDao().getQuizCount() == 0) {
                val quiz1 = Quiz(1, "Sleep", 1, true)
                val quiz2 = Quiz(2, "Wellbeing1", 1, true)
                val quiz3 = Quiz(3, "Wellbeing2", -1, true)
                db.quizDao().insertQuiz(quiz1)
                db.quizDao().insertQuiz(quiz2)
                db.quizDao().insertQuiz(quiz3)
            }
            db.questionDao().insertQuestions(questions)
        }
    }

    suspend fun getQuestionCount(): Int {
        return db.questionDao().getQuestionCount()
    }
}