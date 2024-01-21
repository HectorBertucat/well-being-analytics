package com.example.wellbeinganalytics

import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wellbeinganalytics.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: QuizAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var singOutButton: Button
    private lateinit var copyIdButton: Button
    private lateinit var sendToServer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_list)

        recyclerView = findViewById(R.id.recyclerViewQuizzes)
        viewManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        singOutButton = findViewById(R.id.signOutButton)
        copyIdButton = findViewById(R.id.copyIdButton)
        sendToServer = findViewById(R.id.sendToServerButton)

        singOutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("id")
                remove("name")
                commit()
            }
            startActivity(intent)
        }

        copyIdButton.setOnClickListener {
            val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
            val userId = sharedPref.getString("id", null)
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("userId", userId))
        }

        CoroutineScope(Dispatchers.IO).launch {
            val quizzes = AppDatabase.getDatabase(this@QuizListActivity).quizDao().getActiveQuizzes()
            runOnUiThread {
                viewAdapter = QuizAdapter(quizzes) { quiz ->
                    val intent = Intent(this@QuizListActivity, QuizActivity::class.java)
                    intent.putExtra("quizId", quiz.id)
                    startActivity(intent)
                }

                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
        }
    }
}