package com.jvrcoding.weatherapp.domain.use_case.weather

import com.jvrcoding.weatherapp.common.Resource
import com.jvrcoding.weatherapp.data.remote.*
import com.jvrcoding.weatherapp.data.repository.FakeDataStoreRepository
import com.jvrcoding.weatherapp.data.repository.WeatherRepoImpl
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class GetWeatherUseCaseTest {

    private lateinit var useCase: GetWeatherUseCase
    private lateinit var mockRepository: WeatherRepoImpl
    private lateinit var fakeDataStoreRepository: FakeDataStoreRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        fakeDataStoreRepository = FakeDataStoreRepository()
        useCase = GetWeatherUseCase(mockRepository, fakeDataStoreRepository)
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

        val expected = Resource.Success(weatherDto.toWeather())
        coEvery { mockRepository.getCurrentWeather(lat, lon, username) } returns weatherDto

        val result = useCase(lat, lon).last()

        Truth.assertThat(result.data?.copy(timeCreated = 0L)).isEqualTo(expected.data?.copy(timeCreated = 0L))
    }

    @Test
    fun `get weather with invalid response`() = runBlocking {
        val lat = 999.999
        val lon = 999.999
        val username = "username"
        coEvery { mockRepository.getCurrentWeather(lat, lon, username) } throws HttpException(Response.error<Any>(404, "".toResponseBody()))

        val result = useCase(lat, lon).last()
        Truth.assertThat(result).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `get weather with no internet connection`() = runBlocking {
        val lat = 37.7749
        val lon = -122.4194
        val username = "username"
        val expected = Resource.Error("Couldn't reach server. Check your internet connection.", null)
        coEvery { mockRepository.getCurrentWeather(lat, lon, username) } throws IOException()

        val result = useCase(lat, lon).last()

        Truth.assertThat(result.message).isEqualTo(expected.message)
    }
}