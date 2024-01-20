package com.example.wellbeinganalytics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellbeinganalytics.database.AppDatabase
import com.example.wellbeinganalytics.database.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class QuizAdapter(private val quizzes: List<Quiz>, private val onQuizClick: (Quiz) -> Unit) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewQuizName: TextView = view.findViewById(R.id.textViewQuizName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_item, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]

        CoroutineScope(Dispatchers.IO).launch {
            val lastSentDate = AppDatabase.getDatabase(holder.itemView.context).answerDao().getLastDateFromUserAndQuiz(1, quiz.id)
            val lastSentDateDay = LocalDate.parse(lastSentDate).dayOfYear
            val nbPerDay = AppDatabase.getDatabase(holder.itemView.context).quizDao().getNbPerDayFromQuiz(quiz.id)

            if (nbPerDay == 1) {
                // If the quiz is a daily quiz, we check if the user has already answered today

                todayDay().let { today ->
                    if (lastSentDateDay == today) {
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

    private fun todayDay(): Any {
        return LocalDate.now().dayOfYear
    }

    override fun getItemCount() = quizzes.size
}

