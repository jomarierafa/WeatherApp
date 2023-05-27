package com.example.weatherapp.presentation

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.R
import com.example.weatherapp.common.Screen
import com.example.weatherapp.common.buildAlertDialog
import com.example.weatherapp.common.isAutomaticTimeEnabled
import com.example.weatherapp.presentation.main.MainScreen
import com.example.weatherapp.presentation.login.LoginViewModel
import com.example.weatherapp.presentation.signup.SignupViewModel
import com.example.weatherapp.presentation.login.LoginScreen
import com.example.weatherapp.presentation.main.current_weather.CurrentWeatherViewModel
import com.example.weatherapp.presentation.main.weather_list.WeatherListViewModel
import com.example.weatherapp.presentation.signup.SignupScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!isAutomaticTimeEnabled(this)) {
            buildAlertDialog(
                this,
                getString(R.string.please_set_your_time_automatically),
                getString(R.string.go_to_settings),
            ) {
                startActivity(Intent(Settings.ACTION_DATE_SETTINGS))
            }
        }


        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
                    composable(route = Screen.LoginScreen.route) {
                        val viewModel = hiltViewModel<LoginViewModel>()
                        LoginScreen(
                            navController = navController,
                            state = viewModel.state,
                            uiEvent = viewModel.eventFlow,
                            onEvent = viewModel::onEvent
                        )
                    }

                    composable(route = Screen.SignUpScreen.route) {
                        val viewModel = hiltViewModel<SignupViewModel>()
                        SignupScreen(
                            navController = navController,
                            state = viewModel.state,
                            uiEvent = viewModel.eventFlow,
                            onEvent = viewModel::onEVent
                        )
                    }

                    composable(route = Screen.MainScreen.route) {
                        val currentWeatherViewModel = hiltViewModel<CurrentWeatherViewModel>()
                        val weatherListViewModel = hiltViewModel<WeatherListViewModel>()
                        MainScreen(
                            navController = navController,
                            currentWeatherViewModel = currentWeatherViewModel,
                            weatherListViewModel= weatherListViewModel
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        checkGPS()
        super.onResume()
    }

    private fun checkGPS() {
        if (!isGPSEnabled()) buildAlertMessageNoGps()
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun buildAlertMessageNoGps() {
        buildAlertDialog(
            this,
            getString(R.string.your_gps_seems_to_be_disabled),
            getString(R.string.go_to_settings),
        ) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
}


