package com.example.weatherapp.presentation.main.fragments.current_weather

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.domain.use_case.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
): ViewModel() {

    private val _state = MutableStateFlow(CurrentWeatherState())
    val state: StateFlow<CurrentWeatherState> = _state.asStateFlow()


    fun getCurrentWeather(location: Location?) {
        getWeatherUseCase(location?.latitude ?: 14.6091, location?.longitude ?: 121.0223).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = CurrentWeatherState(weather = result.data)
                }
                is Resource.Error -> {
                    _state.value = CurrentWeatherState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = CurrentWeatherState(isLoading = true)
                }

            }
        }.launchIn(viewModelScope)
    }
}