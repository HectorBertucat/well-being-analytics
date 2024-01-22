package com.example.wellbeinganalytics

import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellbeinganalytics.database.AnswerRepository
import com.example.wellbeinganalytics.database.AppDatabase
import com.example.wellbeinganalytics.database.Quiz
import com.example.wellbeinganalytics.database.QuizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

class QuizAdapter(private val quizzes: List<Quiz>, private val onQuizClick: (Quiz) -> Unit) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    private lateinit var answerRepository: AnswerRepository
    private lateinit var quizRepository: QuizRepository

    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewQuizName: TextView = view.findViewById(R.id.textViewQuizName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        var db = AppDatabase.getDatabase(parent.context)
        answerRepository = AnswerRepository(db)
        quizRepository = QuizRepository(db)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_item, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        // get userId from shared preferences
        val sharedPref = holder.itemView.context.getSharedPreferences("user", MODE_PRIVATE)
        val userId = sharedPref.getString("id", "0")!!

        holder.textViewQuizName.text = quiz.name
        holder.itemView.setOnClickListener { onQuizClick(quiz) }

        CoroutineScope(Dispatchers.IO).launch {
            val lastSentDate = answerRepository.getLastDateFromUserAndQuiz(userId, quiz.id)

            if (lastSentDate == null) {
                holder.itemView.isEnabled = true
                holder.itemView.alpha = 1f
            } else {
                val lastSentDayMonth = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastSentDate.toLong()), java.time.ZoneId.systemDefault()).dayOfMonth
                val nbPerDay = quizRepository.getNbPerDayFromQuiz(quiz.id)
                if (nbPerDay == 1) {
                    todayDayMonth().let { today ->
                        if (lastSentDayMonth == today) {
                            // If the user has already answered today, we disable the quiz
                            holder.itemView.isEnabled = false
                            holder.itemView.alpha = 0.5f
                        } else {
                            // If the user has not already answered today, we enable the quiz
                            holder.itemView.isEnabled = true
                            holder.itemView.alpha = 1f
                        }
                    }
                }
            }
        }
    }

    private fun todayDayMonth(): Any {
        return LocalDate.now().dayOfMonth
    }

    override fun getItemCount() = quizzes.size
}

