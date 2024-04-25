package com.jvrcoding.weatherapp.data.repository


import com.jvrcoding.weatherapp.data.local.WeatherEntity
import com.jvrcoding.weatherapp.domain.model.Weather
import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository: WeatherRepository {

    private val initialWeatherList = mutableListOf<WeatherEntity>()
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        username: String
    ): Result<Weather, DataError> {
        TODO("Not yet implemented")
    }

    override fun getWeathersByUsername(username: String): Flow<Result<List<Weather>, DataError.Local>> = flow {
//        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherById(id: Int) {
        TODO("Not yet implemented")
    }

    fun insertWeather(weather: WeatherEntity) {
        initialWeatherList.add(weather)
    }
}