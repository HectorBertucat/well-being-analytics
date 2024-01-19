package com.example.wellbeinganalytics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    // TODO: check if id is valid, if it is then "login" with id
                }
                if (name.isNotEmpty()) {
                    // TODO: create user with name, create "login" with generated id
                }
                // TODO: show error saying id was not valid
            }
        }
    }
}