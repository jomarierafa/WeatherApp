package com.example.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weather: String,
    val description: String,
    val city: String,
    val country: String,
    val icon: String,
    val temperature: Double,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val username: String,
    val timeCreated: Long
)
