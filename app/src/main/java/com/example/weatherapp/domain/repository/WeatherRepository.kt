package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.remote.WeatherDataDto
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(lat: Double, lon: Double, username: String): WeatherDataDto

    fun getWeathersByUsername(username: String): Flow<List<WeatherEntity>>
}