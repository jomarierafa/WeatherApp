package com.jvrcoding.weatherapp.data.repository

import com.jvrcoding.weatherapp.BuildConfig
import com.jvrcoding.weatherapp.data.local.WeatherDao
import com.jvrcoding.weatherapp.data.remote.WeatherApi
import com.jvrcoding.weatherapp.data.mapper.toWeather
import com.jvrcoding.weatherapp.data.mapper.toWeatherEntity
import com.jvrcoding.weatherapp.data.util.safeCall
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.model.Weather
import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherRepoImpl(
    private val dao: WeatherDao,
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        username: String
    ): Result<Weather, DataError.Network> {
        return safeCall {
            val data = api.getCurrentWeather(lat, lon, BuildConfig.API_KEY)
            data.isSuccessful
            dao.insertWeather(data.body()!!.toWeatherEntity(username))
            data.body()!!.toWeather()
        }
    }

    override fun getWeathersByUsername(username: String): Flow<Result<List<Weather>, DataError.Local>> {
        return dao.getWeathersByUsername(username).map {
            Result.Success(it.map { it.toWeather() })
        }
    }

    override suspend fun deleteWeatherById(id: Int) {
        return dao.deleteWeatherById(id)
    }
}