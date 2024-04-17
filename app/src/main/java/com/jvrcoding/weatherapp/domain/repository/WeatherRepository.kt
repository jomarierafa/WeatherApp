package com.jvrcoding.weatherapp.domain.repository

import com.jvrcoding.weatherapp.data.local.WeatherEntity
import com.jvrcoding.weatherapp.data.remote.WeatherDataDto
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(lat: Double, lon: Double, username: String): WeatherDataDto

    suspend fun getCurrentWeather2(lat: Double, lon: Double, username: String): Result<Weather, DataError>

    fun getWeathersByUsername(username: String): Flow<List<Weather>>

    suspend fun deleteWeatherById(id: Int)
}