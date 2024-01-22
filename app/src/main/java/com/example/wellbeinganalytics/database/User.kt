package com.example.wellbeinganalytics.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User (
    @PrimaryKey val id:String,
    val name: String,
) { }