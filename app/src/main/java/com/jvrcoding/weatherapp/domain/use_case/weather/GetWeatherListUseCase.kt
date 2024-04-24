package com.jvrcoding.weatherapp.domain.use_case.weather

import com.jvrcoding.weatherapp.common.Constant
import com.jvrcoding.weatherapp.domain.model.Weather
import com.jvrcoding.weatherapp.domain.repository.DataStoreRepo
import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
import com.jvrcoding.weatherapp.domain.util.Error
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetWeatherListUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val dataStoreRepo: DataStoreRepo
) {
    operator fun invoke(): Flow<Result<List<Weather>, Error>> {
        val username = runBlocking {
            dataStoreRepo.getString(Constant.USERNAME) ?: ""
        }

        return repository.getWeathersByUsername(username).map { result ->
            result.map { it.sortedByDescending { it.timeCreated } }
        }
    }
}