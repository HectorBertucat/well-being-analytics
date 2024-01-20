package com.example.wellbeinganalytics

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wellbeinganalytics.database.Answer
import com.example.wellbeinganalytics.database.AppDatabase
import com.example.wellbeinganalytics.database.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class QuizActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var currentQuestionIndex = 0
    private lateinit var questions: List<Question>
    private var selectedQuizId: Int = 0
    private val answers = mutableListOf<Answer>()
    private lateinit var textViewQuestion: TextView
    private lateinit var buttonAgree: ImageButton
    private lateinit var buttonNeither: ImageButton
    private lateinit var buttonDisagree: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        selectedQuizId = intent.getIntExtra("quizId", 34)
        Log.e(this::class.java.simpleName, "Quiz id: $selectedQuizId")
        database = AppDatabase.getDatabase(this)

        textViewQuestion = findViewById(R.id.textViewQuestion)
        buttonAgree = findViewById(R.id.imageButtonAgree)
        buttonNeither = findViewById(R.id.imageButtonNeutral)
        buttonDisagree = findViewById(R.id.imageButtonDisagree)

        buttonAgree.setOnClickListener { saveAnswerAndLoadNext(3) }
        buttonNeither.setOnClickListener { saveAnswerAndLoadNext(2) }
        buttonDisagree.setOnClickListener { saveAnswerAndLoadNext(1) }

        loadQuestions()
    }

    private fun loadQuestions() {
        CoroutineScope(Dispatchers.IO).launch {
            questions = database.quizDao().getQuestionsFromQuiz(selectedQuizId)
            Log.e(this::class.java.simpleName, "Questions: $questions")
            withContext(Dispatchers.Main) {
                if (questions.isNotEmpty()) {
                    displayQuestion(questions[currentQuestionIndex])
                } else {
                    // Handle case where there are no questions
                }
            }
        }
    }

    private fun displayQuestion(question: Question) {
        // Display question
        textViewQuestion.text = question.content
    }

    private fun saveAnswerAndLoadNext(answerValue: Int) {
        val userId = getSharedPreferences("user", MODE_PRIVATE).getString("id", "0")!!
        val answer = Answer(0, answerValue, userId, selectedQuizId, questions[currentQuestionIndex].id, Date(), false)
        answers.add(answer)

        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            displayQuestion(questions[currentQuestionIndex])
        } else {
            saveAnswersToDatabase()
        }
    }

    private fun saveAnswersToDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            answers.forEach { database.answerDao().insertAnswer(it) }
            withContext(Dispatchers.Main) {
                // Notify user that quiz is complete, navigate back or to another screen
                // redirect to home
                finish()
            }
        }
    }
}
