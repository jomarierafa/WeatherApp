package com.example.weatherapp.presentation.main.current_weather

import com.example.weatherapp.domain.model.Weather

data class CurrentWeatherState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String = ""
)