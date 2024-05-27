package com.example.lab2.db

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    lateinit var database: AppDatabase

    fun init(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}
