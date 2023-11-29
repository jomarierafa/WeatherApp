package com.jvrcoding.weatherapp.data.repository

import com.jvrcoding.weatherapp.BuildConfig
import com.jvrcoding.weatherapp.data.local.WeatherDao
import com.jvrcoding.weatherapp.data.local.WeatherEntity
import com.jvrcoding.weatherapp.data.remote.WeatherApi
import com.jvrcoding.weatherapp.data.remote.WeatherDataDto
import com.jvrcoding.weatherapp.data.remote.toWeatherEntity
import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val dao: WeatherDao,
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lon: Double, username: String): WeatherDataDto {
        val data = api.getCurrentWeather(lat, lon, BuildConfig.API_KEY)
        if(!data.isSuccessful && data.body() == null) {
            throw HttpException(data)
        }
        dao.insertWeather(data.body()!!.toWeatherEntity(username))
        return data.body()!!
    }

    override fun getWeathersByUsername(username: String): Flow<List<WeatherEntity>> {
        return dao.getWeathersByUsername(username)
    }
}