package com.jvrcoding.weatherapp.domain.repository

import com.jvrcoding.weatherapp.data.local.WeatherEntity
import com.jvrcoding.weatherapp.data.remote.WeatherDataDto
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(lat: Double, lon: Double, username: String): WeatherDataDto

    fun getWeathersByUsername(username: String): Flow<List<WeatherEntity>>
}