package com.jvrcoding.weatherapp.domain.use_case.weather

import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class DeleteWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(id : Int) {
        repository.deleteWeatherById(id)
    }

}