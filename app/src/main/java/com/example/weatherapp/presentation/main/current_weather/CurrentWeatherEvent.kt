package com.example.weatherapp.presentation.main.current_weather

sealed class CurrentWeatherEvent {
    object SwipeRefresh: CurrentWeatherEvent()
}