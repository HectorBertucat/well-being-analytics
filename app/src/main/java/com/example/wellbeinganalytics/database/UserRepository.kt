package com.example.wellbeinganalytics.database

class UserRepository (private val db: AppDatabase) {

    fun getDatabase(): AppDatabase {
        return db
    }

    suspend fun insertUser(user: User) {
        db.userDao().insertUser(user)
    }

    suspend fun getUserById(id: String): User {
        return db.userDao().getUserById(id)
    }

    suspend fun userExists(id: String): Boolean {
        return db.userDao().getUserByIdCount(id) > 0
    }
}