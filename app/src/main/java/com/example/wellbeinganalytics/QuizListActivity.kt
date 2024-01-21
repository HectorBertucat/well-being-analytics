package com.example.wellbeinganalytics

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wellbeinganalytics.database.AnswerRepository
import com.example.wellbeinganalytics.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class QuizListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: QuizAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var singOutButton: Button
    private lateinit var copyIdButton: Button
    private lateinit var sendToServer: Button

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private lateinit var answerRepository: AnswerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_list)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Network is available
                sendDataToServer()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // Network is lost
            }
        }

        // Register network callback
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        val db = AppDatabase.getDatabase(this)
        answerRepository = AnswerRepository(db)

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

        sendToServer.setOnClickListener {
            sendDataToServer()
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

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun sendDataToServer() {
        val client = OkHttpClient()
        var url = "http://10.0.2.2:8080" // localhost from emulator


        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
            val userId = sharedPref.getString("id", null)
            val answersDataJson = getAnswerDataJson()[0]
            val answerIds = getAnswerDataJson()[1]
            val answersIdsInt = ArrayList<Int>()

            for (answerId in answerIds.split(",")) {
                answersIdsInt.add(answerId.toInt())
            }

            Log.e("answerIds", answersIdsInt.toString())

            var finalUrl = "$url?userId=$userId&quizData=$answersDataJson"

            Intent(this@QuizListActivity, WebViewActivity::class.java).also {
                it.putExtra("url", finalUrl)
                it.putExtra("answerIds", answersIdsInt)
                startActivity(it)
            }
        }
    }

    private suspend fun getAnswerDataJson(): List<String> {
        val list = ArrayList<String>();
        val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
        val userId = sharedPref.getString("id", '1'.toString())!!
        val notSentQuizIds = answerRepository.getNotSentQuizIds(userId)
        val answerIds = ArrayList<String>()

        var quizData = "["

        for (quizId in notSentQuizIds) {
            val answers = answerRepository.getNotSentAnswersFromQuizAndUser(quizId, userId)
            var json = "{\"quizId\":$quizId,\"answers\":["
            for (answer in answers) {
                answerIds.add(answer.id.toString())
                json = json.plus("{\"questionId\":${answer.questionId},\"value\":\"${answer.value}\",\"date\":\"${answer.date}\"},")
            }
            json = json.dropLast(1)
            json = json.plus("]},")
            quizData = quizData.plus(json)
        }
        quizData = quizData.dropLast(1)
        quizData = quizData.plus("]")

        Log.e("quizData", quizData)

        list.add(quizData)
        list.add(answerIds.joinToString(","))
        return list
    }
}