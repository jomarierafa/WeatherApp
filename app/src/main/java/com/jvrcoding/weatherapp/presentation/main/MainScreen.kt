package com.jvrcoding.weatherapp.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jvrcoding.weatherapp.presentation.WeatherListScreen
import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.common.Screen
import com.jvrcoding.weatherapp.presentation.main.current_weather.CurrentWeatherScreen
import com.jvrcoding.weatherapp.presentation.main.current_weather.CurrentWeatherViewModel
import com.jvrcoding.weatherapp.presentation.main.weather_list.WeatherListViewModel

@Composable
fun MainScreen(
    navController: NavController,
    currentWeatherViewModel: CurrentWeatherViewModel = hiltViewModel(),
    weatherListViewModel: WeatherListViewModel = hiltViewModel()
) {
    val tabs = listOf(stringResource(R.string.current_weather), stringResource(R.string.weather_list))
    val selectedIndex = remember { mutableIntStateOf(0) }

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
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )

        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            TabRow(
                selectedTabIndex = selectedIndex.intValue,
                backgroundColor = Color.White,
                contentColor = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                color = if(selectedIndex.intValue == index) MaterialTheme.colors.primary else Color.Black
                            )
                        },
                        selected = selectedIndex.intValue == index,
                        onClick = {
                            selectedIndex.intValue = index
                        }
                    )
                }
            }

            when (selectedIndex.intValue) {
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