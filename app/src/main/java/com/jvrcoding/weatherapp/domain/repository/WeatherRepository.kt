package com.jvrcoding.weatherapp.domain.repository

import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(lat: Double, lon: Double, username: String): Result<Weather, DataError>

    fun getWeathersByUsername(username: String): Flow<Result<List<Weather>, DataError.Local>>


    suspend fun deleteWeatherById(id: Int)
}