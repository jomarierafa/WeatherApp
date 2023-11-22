package com.jvrcoding.weatherapp.data.remote

data class WeatherDataDto(
    val weather: List<WeatherDto>,
    val base: String,
    val main: MainDto,
    val visibility: Int,
    val dt: Long,
    val sys: SysDto,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
) {
    constructor(name: String, weather: List<WeatherDto>, main: MainDto, sys: SysDto)
            : this(weather, "", main, 0,0, sys, 0, 0, name, 0)
}

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainDto(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int,
    val grndLevel: Int
) {
    constructor(temp: Double) : this(temp, 0.0, 0.0, 0.0, 0, 0, 0, 0)
}

data class SysDto(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
