package com.jvrcoding.weatherapp.presentation.main.current_weather

sealed class CurrentWeatherEvent {
    data object SwipeRefresh : CurrentWeatherEvent()
}