package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weatherentity")
    fun getWeathers(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weatherentity WHERE username = :username")
    fun getWeathersByUsername(username: String): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weatherentity")
    suspend fun clearAll()
}