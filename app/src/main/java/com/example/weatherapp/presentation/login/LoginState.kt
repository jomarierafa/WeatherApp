package com.example.weatherapp.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val visiblePermissionDialogQueue: MutableList<String>  = mutableListOf()
)