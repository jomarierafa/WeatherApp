package com.jvrcoding.weatherapp.common

sealed class Screen(val route: String, val deeplinkId: Int = -1) {

    object LoginScreen: Screen("login_screen", 0)

    object SignUpScreen: Screen("signup_screen", 1)

    object MainScreen: Screen("main_screen", 2)

}