package com.jvrcoding.weatherapp.domain.use_case.weather


import com.jvrcoding.weatherapp.common.Constant.USERNAME
import com.jvrcoding.weatherapp.data.local.WeatherEntity
import com.jvrcoding.weatherapp.data.repository.FakeDataStoreRepository
import com.jvrcoding.weatherapp.data.repository.FakeWeatherRepository
import com.google.common.truth.Truth
import com.jvrcoding.weatherapp.domain.util.ifSuccess
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

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
        getWeatherList().collectLatest { list ->
            println(list)
            list.ifSuccess { data ->
                for(i in 0 ..data.size - 2) {
                    Truth.assertThat(data[i].timeCreated).isGreaterThan(data[i+1].timeCreated)
                }
            }
        }
    }

    @Test
    fun `get weather list, check if all belong to user`() = runBlocking {
        getWeatherList().collectLatest { list ->
            val username = "username"
            list.ifSuccess { data ->
                Truth.assertThat(data.all { it.username == username}).isTrue()
            }
        }
    }

}