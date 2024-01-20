package com.example.wellbeinganalytics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.wellbeinganalytics.database.AnswerRepository
import com.example.wellbeinganalytics.database.AppDatabase
import com.example.wellbeinganalytics.database.QuestionRepository
import com.example.wellbeinganalytics.database.User
import com.example.wellbeinganalytics.database.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var userReposiory: UserRepository
    private lateinit var questionRepository: QuestionRepository
    private lateinit var answerRepository: AnswerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getDatabase(this)
        questionRepository = QuestionRepository(db)
        userReposiory = UserRepository(db)
        answerRepository = AnswerRepository(db)

        CoroutineScope(Dispatchers.IO).launch {
            initializeDb()
        }

        val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
        val sharedId = sharedPref.getString("id", null)

        CoroutineScope(Dispatchers.IO).launch {
            if (sharedId != null) {
                val currentUser = userReposiory.getUserById(sharedId)
                if (currentUser == null) {
                    val sharedName = sharedPref.getString("name", null)
                    userReposiory.insertUser(User(sharedId, sharedName!!))
                }
                Intent(this@MainActivity, QuizListActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

        val editTextUserName = findViewById<EditText>(R.id.editTextUserName)
        val editTextUserId = findViewById<EditText>(R.id.editTextUserId)
        val buttonStart = findViewById<Button>(R.id.buttonStart)

        buttonStart.setOnClickListener {
            val id = editTextUserId.text.toString()
            val name = editTextUserName.text.toString()

            if (id.isEmpty() && name.isEmpty()) { // if both fields are empty
                return@setOnClickListener
            } else {
                if (id.isNotEmpty()) {
                    // check if id is valid, if it is then "login" with id (room)
                    CoroutineScope(Dispatchers.IO).launch {
                        val idExists = userReposiory.userExists(id)
                        if (idExists) {
                            val currentUser = userReposiory.getUserById(id)
                            val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("id", currentUser.id)
                                putString("name", currentUser.name)
                                apply()
                            }
                        }
                    }
                }
                if (name.isNotEmpty()) {
                    // check if name is valid, if it is then "login" with name (room)
                    CoroutineScope(Dispatchers.IO).launch {
                        val userToInsert = User(java.util.UUID.randomUUID().toString(), name)
                        userReposiory.insertUser(userToInsert)
                        val currentUser = userReposiory.getUserById(userToInsert.id)
                        val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("id", currentUser.id)
                            putString("name", currentUser.name)
                            apply()
                        }
                    }
                }
            }
            Intent(this@MainActivity, QuizListActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private suspend fun initializeDb() {
        if (questionRepository.getQuestionCount() == 0) {
            questionRepository.insertQuestions()
        }
        Log.d("Answers", answerRepository.getNotSentAnswers().toString())
    }
}