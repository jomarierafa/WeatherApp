package com.example.weatherapp.domain.use_case.weather


import com.example.weatherapp.common.Constant.USERNAME
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.repository.FakeDataStoreRepository
import com.example.weatherapp.data.repository.FakeWeatherRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.math.log

class GetWeatherListUseCaseTest {

    private lateinit var getWeatherList: GetWeatherListUseCase
    private lateinit var fakeRepository: FakeWeatherRepository
    private lateinit var fakeDataStoreRepository: FakeDataStoreRepository

    @Before
    fun setUp() {
        fakeRepository = FakeWeatherRepository()
        fakeDataStoreRepository = FakeDataStoreRepository()
        getWeatherList = GetWeatherListUseCase(fakeRepository, fakeDataStoreRepository)

        val weatherToInsert = mutableListOf<WeatherEntity>()
        ('a'..'z').forEachIndexed { index, c ->
            weatherToInsert.add(
                WeatherEntity(
                    id = index,
                    weather = "$c weather",
                    description = "$c description",
                    city = "$c city",
                    country = "$c country",
                    icon = "$c icon",
                    username = "username",
                    temperature = index.toDouble(),
                    sunriseTime = index.toLong(),
                    sunsetTime = index.toLong(),
                    timeCreated = index.toLong()
                )
            )
        }

        weatherToInsert.shuffle()
        weatherToInsert.forEach {
            fakeRepository.insertWeather(it)
        }
        runBlocking {
            fakeDataStoreRepository.putString(USERNAME, "username")
        }
    }

    @Test
    fun `get Weather list by time created in descending order`() = runBlocking {
        val list = getWeatherList().first()

        for(i in 0 ..list.size - 2) {
            Truth.assertThat(list[i].timeCreated).isGreaterThan(list[i+1].timeCreated)
        }
    }

    @Test
    fun `get weather list, check if all belong to user`() = runBlocking {
        val list = getWeatherList().first()
        val username = "username"
        Truth.assertThat(list.all { it.username == username}).isFalse()
    }

}