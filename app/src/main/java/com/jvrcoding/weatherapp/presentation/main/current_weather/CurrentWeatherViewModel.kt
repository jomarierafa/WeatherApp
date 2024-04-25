package com.jvrcoding.weatherapp.presentation.main.current_weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.weatherapp.domain.use_case.weather.GetWeatherUseCase
import com.jvrcoding.weatherapp.domain.util.ifError
import com.jvrcoding.weatherapp.domain.util.ifSuccess
import com.jvrcoding.weatherapp.presentation.util.UiText
import com.jvrcoding.weatherapp.presentation.util.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    @ApplicationContext context: Context
) : ViewModel() {

    private val locationManager: LocationManager by lazy {
        context.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private val _state = MutableStateFlow(CurrentWeatherState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val locationCallback = LocationListener { }

    //TODO("check alternative way to call this part")
    init {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdate()
        }
    }

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

    fun onEvent(event: CurrentWeatherEvent) {
        when (event) {
            CurrentWeatherEvent.SwipeRefresh -> {
                getCurrentWeather()
            }
        }
    }

    fun getCurrentWeather() {
        val location = getCurrentLocation()
        //TODO("check loading implementation")
        _state.value = _state.value.copy(isLoading = true)
        getWeatherUseCase(location?.latitude, location?.longitude).onEach { result ->
            result.ifSuccess { data ->
                _state.value = CurrentWeatherState(weather = data)
            }.ifError { error ->
                _state.value = _state.value.copy(isLoading = false)
                val errorMessage = error.asUiText()
                _eventFlow.emit(UiEvent.Error(errorMessage))
            }

        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        locationManager.removeUpdates(locationCallback)
    }

    sealed class UiEvent {
        data class Error(val error: UiText) : UiEvent()
    }
}