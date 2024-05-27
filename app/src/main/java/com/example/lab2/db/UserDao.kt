package com.example.lab2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result)

    @Update
    suspend fun updateResult(result: Result)

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM Result WHERE email = :email")
    suspend fun getResultsByEmail(email: String): List<Result>

    @Query("SELECT * FROM Result")
    suspend fun getAllResults(): List<Result>

    @Query("SELECT * FROM Result WHERE email = :email ORDER BY score ASC LIMIT 1")
    suspend fun getBestResultByEmail(email: String): Result?

    @Query("DELETE FROM Result")
    suspend fun deleteAllResults()
}
