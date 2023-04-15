package com.example.weatherapp.domain.use_case.weather

import com.example.weatherapp.common.Constant.USERNAME
import com.example.weatherapp.data.remote.toWeather
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.DataStoreRepo
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetWeatherListUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val dataStoreRepo: DataStoreRepo
) {
     operator fun invoke(): Flow<List<Weather>> {
         val username = runBlocking {
             dataStoreRepo.getString(USERNAME) ?: ""
         }
        return repository.getWeathersByUsername(username).map {
            it.map { it.toWeather() }.sortedByDescending { it.timeCreated }
        }
    }
}