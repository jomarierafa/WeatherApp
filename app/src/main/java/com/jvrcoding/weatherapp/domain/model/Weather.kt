package com.jvrcoding.weatherapp.domain.model

data class Weather(
    val id: Int,
    val weather: String,
    val description: String,
    val city: String,
    val country: String,
    val icon: String,
    val temperature: Double,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val username: String?= "",
    val timeCreated: Long
)
