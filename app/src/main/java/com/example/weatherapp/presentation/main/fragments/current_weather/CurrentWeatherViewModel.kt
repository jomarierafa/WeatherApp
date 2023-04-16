package com.example.weatherapp.presentation.main.fragments.current_weather

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.Resource
import com.example.weatherapp.domain.use_case.weather.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    @ApplicationContext context: Context
): ViewModel() {

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val _state = MutableStateFlow(CurrentWeatherState())
    val state: StateFlow<CurrentWeatherState> = _state.asStateFlow()

    @SuppressLint("MissingPermission")
    fun startLocationUpdate() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 5000, 10f, locationCallback
        )
        getCurrentWeather()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
    }

    private val locationCallback = object : LocationListener {
        override fun onLocationChanged(location: Location) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    fun getCurrentWeather() {
        val location = getCurrentLocation()
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

    override fun onCleared() {
        super.onCleared()
        locationManager.removeUpdates(locationCallback)
    }
}