package com.example.weatherapp.presentation.main.weather_list

import com.example.weatherapp.domain.model.Weather

data class WeatherListState(
    val weathers: List<Weather> = emptyList(),
    val error: String = ""
)