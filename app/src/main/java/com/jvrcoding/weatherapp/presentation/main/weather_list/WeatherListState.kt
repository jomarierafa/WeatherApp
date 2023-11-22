package com.jvrcoding.weatherapp.presentation.main.weather_list

import com.jvrcoding.weatherapp.domain.model.Weather

data class WeatherListState(
    val weathers: List<Weather> = emptyList(),
    val error: String = ""
)