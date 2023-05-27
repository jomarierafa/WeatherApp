package com.example.weatherapp.presentation.signup

import com.example.weatherapp.data.local.User

data class SignupState(
    val firstname: String = "",
    val lastname: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

fun SignupState.toUser(): User {
    return User(
        firstname = firstname,
        lastname = lastname,
        username = username,
        password = password,
        confirmPassword = confirmPassword
    )
}