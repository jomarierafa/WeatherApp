package com.jvrcoding.weatherapp.data.repository


import com.jvrcoding.weatherapp.data.local.WeatherEntity
import com.jvrcoding.weatherapp.data.remote.WeatherDataDto
import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository: WeatherRepository {

    private val initialWeatherList = mutableListOf<WeatherEntity>()

    fun insertWeather(weather: WeatherEntity) {
        initialWeatherList.add(weather)
    }

    override fun getWeathersByUsername(username: String): Flow<List<WeatherEntity>> = flow {
        emit(initialWeatherList.filter { it.username == username })
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        username: String
    ): WeatherDataDto {
        TODO("Not yet implemented")
    }

}