package com.example.lab2.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Result::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}