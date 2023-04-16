package com.example.weatherapp.presentation.main.fragments.current_weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.common.*
import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.domain.model.Weather
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private val viewModel: CurrentWeatherViewModel by viewModels()
    private lateinit var binding: FragmentCurrentWeatherBinding

    private val locationManager: LocationManager by lazy {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserver()
        startLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        checkGPS()
    }

    private fun initViews(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCurrentWeather(getCurrentLocation())
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { currentState ->
                currentState.weather?.let {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    setViews(it)
                }
                if(currentState.error.isNotBlank()) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    requireContext().longToast(currentState.error)
                }
                if(currentState.isLoading) {

                }
            }
        }
    }

    private fun checkGPS() {
        if (!isGPSEnabled()) buildAlertMessageNoGps()
    }

    private fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun buildAlertMessageNoGps() {
        buildAlertDialog(
            requireContext(),
            getString(R.string.your_gps_seems_to_be_disabled),
            getString(R.string.go_to_settings),
        ) {
            requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }


    private val locationCallback = object : LocationListener {
        override fun onLocationChanged(location: Location) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10f, locationCallback
            )
            viewModel.getCurrentWeather(getCurrentLocation())
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
    }

    @SuppressLint("SetTextI18n")
    private fun setViews(weather: Weather) {
        binding.image.load("${Constant.OPEN_WEATHER_IMAGE_URL}${weather.icon}@4x.png")

        val celsiusSymbol = getString(R.string.celsius)
        binding.tempText.text = "${weather.temperature.kelvinToCelsius()} $celsiusSymbol"

        binding.currentTimeText.text = "Today ${weather.timeCreated.epochToString("h:mm a")}"
        binding.descText.text = weather.description
        binding.locationText.text = "${weather.city}, ${weather.country.getCountryName()}"
        binding.sunriseText.text = "Sunrise ${weather.sunriseTime.convertUtcToLocaleTime()}"
        binding.sunsetText.text = "Sunset ${weather.sunsetTime.convertUtcToLocaleTime()}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationManager.removeUpdates(locationCallback)
    }

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }
}