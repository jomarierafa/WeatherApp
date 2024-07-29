package com.jvrcoding.weatherapp.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val visiblePermissionDialogQueue: MutableList<String>  = mutableListOf()
)