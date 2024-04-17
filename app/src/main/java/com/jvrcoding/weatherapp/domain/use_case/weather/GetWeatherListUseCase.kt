package com.jvrcoding.weatherapp.domain.use_case.weather

import com.jvrcoding.weatherapp.common.Constant.USERNAME
import com.jvrcoding.weatherapp.domain.model.Weather
import com.jvrcoding.weatherapp.domain.repository.DataStoreRepo
import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
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
            it.sortedByDescending { it.timeCreated }
        }
    }
}