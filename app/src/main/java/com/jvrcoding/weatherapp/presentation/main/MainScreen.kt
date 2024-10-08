package com.jvrcoding.weatherapp.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jvrcoding.weatherapp.presentation.main.weather_list.WeatherListScreen
import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.presentation.main.current_weather.CurrentWeatherScreen
import com.jvrcoding.weatherapp.presentation.main.current_weather.CurrentWeatherViewModel
import com.jvrcoding.weatherapp.presentation.main.weather_list.WeatherListViewModel
import com.jvrcoding.weatherapp.presentation.screen.Login

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    currentWeatherViewModel: CurrentWeatherViewModel = hiltViewModel(),
    weatherListViewModel: WeatherListViewModel = hiltViewModel()
) {
    val tabs = listOf(stringResource(R.string.current_weather), stringResource(R.string.weather_list))
    val selectedIndex = remember { mutableIntStateOf(0) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text(stringResource(R.string.welcome)) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Login) {
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
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
                    .windowInsetsPadding(WindowInsets(0, 0 ,0 ,0))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                color = if(selectedIndex.intValue == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onBackground
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
                    val state by currentWeatherViewModel.state.collectAsStateWithLifecycle()
                    CurrentWeatherScreen(
                        state = state,
                        onEvent = currentWeatherViewModel::onEvent,
                        uiEvent = currentWeatherViewModel.eventFlow
                    )
                }
                1 -> {
                    val state by weatherListViewModel.state.collectAsState()
                    WeatherListScreen(
                        state = state,
                        onEvent = weatherListViewModel::onEvent
                    )
                }
            }

        }
    }

}