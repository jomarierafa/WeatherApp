package com.example.weatherapp.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.common.Screen
import com.example.weatherapp.presentation.WeatherListScreen
import com.example.weatherapp.presentation.main.current_weather.CurrentWeatherScreen
import com.example.weatherapp.presentation.main.current_weather.CurrentWeatherViewModel
import com.example.weatherapp.presentation.main.weather_list.WeatherListViewModel

@Composable
fun MainScreen(
    navController: NavController,
    currentWeatherViewModel: CurrentWeatherViewModel = hiltViewModel(),
    weatherListViewModel: WeatherListViewModel = hiltViewModel()
) {
    val tabs = listOf(stringResource(R.string.current_weather), stringResource(R.string.weather_list))
    val selectedIndex = remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.welcome)) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )

        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            TabRow(
                selectedTabIndex = selectedIndex.value,
                backgroundColor = Color.White,
                contentColor = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                color = if(selectedIndex.value == index) MaterialTheme.colors.primary else Color.Black
                            )
                        },
                        selected = selectedIndex.value == index,
                        onClick = {
                            selectedIndex.value = index
                        }
                    )
                }
            }

            when (selectedIndex.value) {
                0 -> {
                    val state by currentWeatherViewModel.state.collectAsState()
                    CurrentWeatherScreen(
                        state = state,
                        onEvent = currentWeatherViewModel::onEvent
                    )
                }
                1 -> {
                    val state by weatherListViewModel.state.collectAsState()
                    WeatherListScreen(
                        state = state
                    )
                }
            }

        }
    }

}