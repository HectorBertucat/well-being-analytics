package com.example.wellbeinganalytics

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.wellbeinganalytics.database.AppDatabase
import com.example.wellbeinganalytics.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "wellbeing_database"
        ).build()

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
                    // check if id is valid, if it is then "login" with id (room)
                    CoroutineScope(Dispatchers.IO).launch {
                        val idExists = db.userDao().getUserById(id)
                        if (idExists != null) {
                            val currentUser = db.userDao().getUserById(id)
                            val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("id", currentUser.id)
                                putString("name", currentUser.name)
                                apply()
                            }
                            // redirect to quiz list
                            Intent(this@MainActivity, QuizListActivity::class.java).also {
                                startActivity(it)
                            }
                        }
                    }
                }
                if (name.isNotEmpty()) {
                    // check if name is valid, if it is then "login" with name (room)
                    CoroutineScope(Dispatchers.IO).launch {
                        val userToInsert = User(java.util.UUID.randomUUID().toString(), name)
                        db.userDao().insertUser(userToInsert)
                        val currentUser = db.userDao().getUserById(userToInsert.id)
                        val sharedPref = getSharedPreferences("user", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("id", currentUser.id)
                            putString("name", currentUser.name)
                            apply()
                        }
                        Intent(this@MainActivity, QuizListActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                }
            }
        }
    }
}