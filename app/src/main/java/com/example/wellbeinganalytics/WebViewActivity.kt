package com.example.wellbeinganalytics

import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.wellbeinganalytics.database.AnswerRepository
import com.example.wellbeinganalytics.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var answerRepository: AnswerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val db = AppDatabase.getDatabase(this)
        answerRepository = AnswerRepository(db)

        webView = findViewById(R.id.webView)
        val buttonBack = findViewById<Button>(R.id.backButton)

        buttonBack.setOnClickListener {
            // Navigate back to QuizListActivity
            Intent(this, QuizListActivity::class.java).also {
                startActivity(it)
            }
        }

        webView.apply {
            clearCache(true)
            clearHistory()
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            // Load the data
        }

        val answerIds = intent.getStringArrayListExtra("answerIds")!!

        val url = intent.getStringExtra("url")!!

        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(android.content.ClipData.newPlainText("url", url))

        fetchAndSaveHtml(url)

        CoroutineScope(Dispatchers.IO).launch {
            for (answerId in answerIds) {
                val int = answerId.toInt()
                val done = answerRepository.setAnswerSent(int)
            }
        }
    }

    private fun fetchAndSaveHtml(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url) // Replace with your URL
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { htmlContent ->
                    runOnUiThread {
                        displayHtmlContent(htmlContent)
                    }
                }
            }
        })
    }

    private fun displayHtmlContent(htmlContent: String) {
        webView.loadData(htmlContent, "text/html", "UTF-8")
    }
}
