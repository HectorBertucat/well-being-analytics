package com.example.wellbeinganalytics

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wellbeinganalytics.database.Answer
import com.example.wellbeinganalytics.database.AnswerRepository
import com.example.wellbeinganalytics.database.AppDatabase
import com.example.wellbeinganalytics.database.Question
import com.example.wellbeinganalytics.database.QuestionRepository
import com.example.wellbeinganalytics.database.QuizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class QuizActivity : AppCompatActivity() {

    private lateinit var questionRepository: QuestionRepository
    private lateinit var quizRepository: QuizRepository
    private lateinit var answerRepository: AnswerRepository
    private var currentQuestionIndex = 0
    private lateinit var questions: List<Question>
    private var selectedQuizId: Int = 0
    private val answers = mutableListOf<Answer>()

    private lateinit var textViewQuestion: TextView
    private lateinit var textViewQuizName: TextView
    private lateinit var textViewQuestionNumber: TextView
    private lateinit var buttonAgree: ImageButton
    private lateinit var buttonNeither: ImageButton
    private lateinit var buttonDisagree: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        selectedQuizId = intent.getIntExtra("quizId", 34)
        val db = AppDatabase.getDatabase(this)
        questionRepository = QuestionRepository(db)
        quizRepository = QuizRepository(db)
        answerRepository = AnswerRepository(db)

        textViewQuestion = findViewById(R.id.textViewQuestion)
        textViewQuizName = findViewById(R.id.textViewQuizName)
        textViewQuestionNumber = findViewById(R.id.textViewQuestionNumber)
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
            questions = quizRepository.getQuestionsFromQuiz(selectedQuizId)
            textViewQuizName.text = quizRepository.getQuizById(selectedQuizId).name
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
        textViewQuestionNumber.text = "${currentQuestionIndex + 1} / ${questions.size}"
    }

    private fun saveAnswerAndLoadNext(answerValue: Int) {
        val userId = getSharedPreferences("user", MODE_PRIVATE).getString("id", "0")!!
        val answer = Answer(answerValue, userId, selectedQuizId, questions[currentQuestionIndex].id, Date(), false)
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
            answerRepository.insertAnswers(answers)
            withContext(Dispatchers.Main) {
                // Notify user that quiz is complete, navigate back or to another screen
                // redirect to home
                Intent(this@QuizActivity, QuizListActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }
}
