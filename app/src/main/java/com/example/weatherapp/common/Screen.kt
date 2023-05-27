package com.example.weatherapp.common

sealed class Screen(val route: String) {

    object LoginScreen: Screen("login_screen")

    object SignUpScreen: Screen("signup_screen")

    object MainScreen: Screen("main_screen")

}