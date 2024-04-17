package com.jvrcoding.weatherapp.presentation.util

sealed class Screen(val route: String, val deeplinkId: Int = -1) {

    data object LoginScreen: Screen("login_screen", 0)

    data object SignUpScreen: Screen("signup_screen", 1)

    data object MainScreen: Screen("main_screen", 2)

}