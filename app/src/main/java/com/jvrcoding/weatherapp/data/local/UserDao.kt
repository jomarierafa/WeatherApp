package com.jvrcoding.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT COUNT(*) FROM userentity")
    suspend fun getUserCount(): Int

}