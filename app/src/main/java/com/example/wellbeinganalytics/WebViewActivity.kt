package com.example.wellbeinganalytics

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webView)
        val buttonBack = findViewById<Button>(R.id.backButton)

        buttonBack.setOnClickListener {
            // Navigate back to QuizListActivity
            finish()
        }

        // val answerIds = intent.getIntArrayExtra("answerIds")!!
        val url = intent.getStringExtra("url")!!

        // Load the data into the WebView as needed

        webView.loadUrl(url)
    }

    // ... rest of the WebViewActivity code ...
}
