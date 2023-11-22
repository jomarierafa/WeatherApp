package com.jvrcoding.weatherapp.presentation.main.current_weather

sealed class CurrentWeatherEvent {
    object SwipeRefresh: CurrentWeatherEvent()
}