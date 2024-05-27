package com.example.lab2.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val score: Int,
    val colorCount: Int
)