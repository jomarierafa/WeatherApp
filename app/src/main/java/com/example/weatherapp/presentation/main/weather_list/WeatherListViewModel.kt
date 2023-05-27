package com.example.weatherapp.presentation.main.weather_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.use_case.weather.GetWeatherListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private var getWeatherListUseCase: GetWeatherListUseCase
): ViewModel() {

    private val _state = MutableStateFlow(WeatherListState())
    val state: StateFlow<WeatherListState> = _state.asStateFlow()

    init {
        getWeatherList()
    }

    private fun getWeatherList() {
        getWeatherListUseCase().onEach { list ->
            _state.value = state.value.copy(weathers = list)
        }.launchIn(viewModelScope)
    }

}