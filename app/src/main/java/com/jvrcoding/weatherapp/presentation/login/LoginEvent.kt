package com.jvrcoding.weatherapp.presentation.login

sealed class LoginEvent {

    data class UsernameChanged(val value: String): LoginEvent()

    data class PasswordChanged(val value: String): LoginEvent()

    data class OnPermissionResult(val permission: String, val isGranted: Boolean): LoginEvent()

    data class TogglePasswordVisibility(val isPasswordVisible: Boolean) : LoginEvent()

    data object DismissDialog: LoginEvent()

    data object Login: LoginEvent()

}