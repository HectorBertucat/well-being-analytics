package com.example.wellbeinganalytics

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

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
        // TODO: Handle sending data to server and displaying the page
        // TODO: After sending data to server, set all of the answers to isSent = true
    }
}