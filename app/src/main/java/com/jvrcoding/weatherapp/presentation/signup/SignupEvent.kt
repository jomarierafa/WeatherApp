package com.jvrcoding.weatherapp.presentation.signup

sealed class SignupEvent {

    data class FirstnameChanged(val value: String): SignupEvent()

    data class LastnameChanged(val value: String): SignupEvent()

    data class UsernameChanged(val value: String): SignupEvent()

    data class PasswordChanged(val value: String): SignupEvent()

    data class ConfirmPasswordChanged(val value: String): SignupEvent()

    data class TogglePasswordVisibility(val isPasswordVisible: Boolean) : SignupEvent()

    data class ToggleConfirmPasswordVisibility(val isConfirmPasswordVisible: Boolean) : SignupEvent()

    data object Signup: SignupEvent()
}