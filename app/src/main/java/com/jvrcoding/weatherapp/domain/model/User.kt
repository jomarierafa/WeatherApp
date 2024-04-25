package com.jvrcoding.weatherapp.domain.model

data class User(
    val firstname: String,
    val lastname: String,
    val username: String,
    val password: String,
    val confirmPassword: String
)