package com.jvrcoding.weatherapp.domain.use_case.weather

import com.jvrcoding.weatherapp.common.Constant
import com.jvrcoding.weatherapp.domain.util.Error
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.util.UserDataValidator
import com.jvrcoding.weatherapp.domain.model.Weather
import com.jvrcoding.weatherapp.domain.repository.DataStoreRepo
import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeatherUseCase2 @Inject constructor(
    private val repository: WeatherRepository,
    private val dataStoreRepo: DataStoreRepo,
    private val userDataValidator: UserDataValidator
) {

    operator fun invoke(lat: Double?, lon: Double?): Flow<Result<Weather, Error>> = flow {
        when(val result = userDataValidator.validateLocation(lat, lon)) {
            is Result.Error -> {
                when(result.error) {
                    UserDataValidator.UserDataError.LOCATION_ERROR -> {
                        emit(Result.Error(result.error))
                    }
                }
            }
            is Result.Success -> {
                val username = dataStoreRepo.getString(Constant.USERNAME) ?: ""
                emit(repository.getCurrentWeather2(lat!!, lon!!, username))
            }
        }
    }
}