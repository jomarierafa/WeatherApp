package com.jvrcoding.weatherapp.presentation.main.current_weather

import com.jvrcoding.weatherapp.domain.model.Weather

data class CurrentWeatherState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String = ""
)