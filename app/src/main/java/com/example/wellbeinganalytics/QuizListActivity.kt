package com.example.wellbeinganalytics

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_list)

        recyclerView = findViewById(R.id.recyclerViewQuizzes)

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