package com.jvrcoding.weatherapp.presentation.main.weather_list

sealed class WeatherListEvent {
    data class DeleteWeather(val id: Int, val animationDuration: Int = 0) : WeatherListEvent()
}