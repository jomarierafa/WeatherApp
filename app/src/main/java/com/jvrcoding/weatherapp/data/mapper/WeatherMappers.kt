package com.jvrcoding.weatherapp.data.mapper

import com.jvrcoding.weatherapp.data.local.WeatherEntity
import com.jvrcoding.weatherapp.data.remote.WeatherDataDto
import com.jvrcoding.weatherapp.domain.model.Weather
import java.time.Instant

fun WeatherDataDto.toWeatherEntity(username: String) : WeatherEntity {
    return WeatherEntity(
        weather = weather[0].main,
        description = weather[0].description,
        city = name,
        country = sys.country,
        icon =  weather[0].icon,
        temperature = main.temp,
        sunriseTime = sys.sunrise,
        sunsetTime = sys.sunset,
        username = username,
        timeCreated = Instant.now().epochSecond
    )
}

fun WeatherDataDto.toWeather() : Weather {
    return Weather(
        id = 0,
        weather = weather[0].main,
        description = weather[0].description,
        city = name,
        country = sys.country,
        icon =  weather[0].icon,
        temperature = main.temp,
        sunriseTime = sys.sunrise,
        sunsetTime = sys.sunset,
        timeCreated = Instant.now().epochSecond
    )
}
fun WeatherEntity.toWeather() : Weather {
    return Weather(
        id = id,
        weather = weather,
        description = description,
        city = city,
        country = country,
        icon = icon,
        temperature = temperature,
        sunriseTime = sunriseTime,
        sunsetTime = sunsetTime,
        username = username,
        timeCreated = timeCreated
    )
}