package com.example.lab2.db
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class User(
    @PrimaryKey val email: String,
    val name: String,
    val avatarUri: String?
)
