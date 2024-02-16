package com.jvrcoding.weatherapp.presentation.main.weather_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.weatherapp.domain.use_case.weather.DeleteWeatherUseCase
import com.jvrcoding.weatherapp.domain.use_case.weather.GetWeatherListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private var getWeatherListUseCase: GetWeatherListUseCase,
    private var deleteWeatherUseCase: DeleteWeatherUseCase
): ViewModel() {

    private val _state = MutableStateFlow(WeatherListState())
    val state: StateFlow<WeatherListState> = _state.asStateFlow()

    init {
        getWeatherList()
    }

    fun onEvent(event: WeatherListEvent) {
        when(event) {
            is WeatherListEvent.DeleteWeather -> {
                viewModelScope.launch {
                    delay(event.animationDuration.toLong())
                    deleteWeatherUseCase(id = event.id)
                }
            }
        }

    }

    private fun getWeatherList() {
        getWeatherListUseCase().onEach { list ->
            _state.value = state.value.copy(weathers = list)
        }.launchIn(viewModelScope)
    }

}