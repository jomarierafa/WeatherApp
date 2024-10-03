package com.jvrcoding.weatherapp.presentation.main.weather_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jvrcoding.weatherapp.presentation.util.SwipeToDeleteContainer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherListScreen(
    state: WeatherListState, onEvent: (WeatherListEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = state.weathers, key = { it.id }) { weather ->
                SwipeToDeleteContainer(
                    onDelete = { animationDuration ->
                        onEvent(WeatherListEvent.DeleteWeather(weather.id, animationDuration))
                    },
                    onEdit = { },
                    modifier = Modifier.animateItemPlacement()
                ) {
                    WeatherItem(
                        weather = weather,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background((MaterialTheme.colorScheme.background))
                    )
                }
            }
        }
    }
}