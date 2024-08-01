package com.jvrcoding.weatherapp.domain.use_case.weather

import com.jvrcoding.weatherapp.data.remote.*
import com.jvrcoding.weatherapp.data.repository.FakeDataStoreRepository
import com.jvrcoding.weatherapp.data.repository.WeatherRepoImpl
import com.google.common.truth.Truth
import com.jvrcoding.weatherapp.data.mapper.toWeather
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.util.UserDataValidator
import com.jvrcoding.weatherapp.domain.util.ifSuccess
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetWeatherUseCaseTest {

    private lateinit var useCase: GetWeatherUseCase
    private lateinit var mockRepository: WeatherRepoImpl
    private lateinit var fakeDataStoreRepository: FakeDataStoreRepository
    private lateinit var userDataValidator: UserDataValidator

    @Before
    fun setUp() {
        mockRepository = mockk()
        fakeDataStoreRepository = FakeDataStoreRepository()
        userDataValidator = UserDataValidator()
        useCase = GetWeatherUseCase(mockRepository, fakeDataStoreRepository, userDataValidator)
    }

    @Test
    fun `get weather with valid coordinates`() = runBlocking {
        val lat = 37.7749
        val lon = -122.4194
        val username = "username"
        val weatherDto = WeatherDataDto(
            name = "Nasugbu",
            weather = listOf(WeatherDto(0, "cloud", "scattered cloud", "01d")),
            main = MainDto(375.30),
            sys = SysDto("PH", 30L, 40L)
        )

        val expected = Result.Success(weatherDto.toWeather())
        coEvery { mockRepository.getCurrentWeather(lat, lon, username) } returns Result.Success(weatherDto.toWeather())

        useCase(lat, lon).collectLatest {
            it.ifSuccess { data ->
                Truth.assertThat(data.copy(timeCreated = 0L)).isEqualTo(expected.data?.copy(timeCreated = 0L))
            }
        }


    }

    @Test
    fun `get weather with invalid response`() = runBlocking {
        val lat = 999.999
        val lon = 999.999
        val username = "username"
        coEvery { mockRepository.getCurrentWeather(lat, lon, username) } returns Result.Error(DataError.Network.NOT_FOUND)

        useCase(lat, lon).collectLatest { error ->
            Truth.assertThat(error).isEqualTo(Result.Error(DataError.Network.NOT_FOUND))
        }
    }

    @Test
    fun `get weather with no internet connection`() = runBlocking {
        val lat = 37.7749
        val lon = -122.4194
        val username = "username"
        coEvery { mockRepository.getCurrentWeather(lat, lon, username) }returns Result.Error(DataError.Network.NO_INTERNET)

        useCase(lat, lon).collectLatest { error ->
            Truth.assertThat(error).isEqualTo(Result.Error(DataError.Network.NO_INTERNET))
        }

    }
}