package com.example.weatherapp.domain.use_case.weather

import com.example.weatherapp.common.Constant.USERNAME
import com.example.weatherapp.common.Resource
import com.example.weatherapp.data.remote.toWeather
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.DataStoreRepo
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val dataStoreRepo: DataStoreRepo
) {

    operator fun invoke(lat: Double, lon: Double): Flow<Resource<Weather>> = flow {
        try {
            emit(Resource.Loading())
            val username = dataStoreRepo.getString(USERNAME) ?: ""
            val weather = repository.getCurrentWeather(lat, lon, username).toWeather()
            emit(Resource.Success(weather))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }  catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}