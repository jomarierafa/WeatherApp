package com.jvrcoding.weatherapp.presentation.signup

import com.jvrcoding.weatherapp.domain.model.User

data class SignupState(
    val firstname: String = "",
    val lastname: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false
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