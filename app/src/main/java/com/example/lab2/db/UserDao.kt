package com.example.lab2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result)

    @Query("SELECT * FROM result ORDER BY score ASC")
    suspend fun getAllResults(): List<Result>

    @Query("DELETE FROM result")
    suspend fun deleteAllResults()

    @Query("SELECT * FROM result WHERE email = :email ORDER BY score ASC LIMIT 1")
    suspend fun getBestResultByEmail(email: String): Result?
}
