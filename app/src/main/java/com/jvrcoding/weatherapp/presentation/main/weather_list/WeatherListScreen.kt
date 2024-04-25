package com.jvrcoding.weatherapp.presentation.main.weather_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jvrcoding.weatherapp.presentation.util.SwipeToDeleteContainer

@Composable
fun WeatherListScreen(
    state: WeatherListState, onEvent: (WeatherListEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = state.weathers, key = { it.id }) { weather ->
                SwipeToDeleteContainer(item = weather, onDelete = { _, animationDuration ->
                    onEvent(WeatherListEvent.DeleteWeather(weather.id, animationDuration))
                }) {
                    WeatherItem(
                        weather = weather,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                }
            }
        }
    }
}