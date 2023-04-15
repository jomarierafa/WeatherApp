package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.Constant
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.data.repository.DataStoreRepoImpl
import com.example.weatherapp.data.repository.UserRepositoryImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.DataStoreRepo
import com.example.weatherapp.domain.repository.UserRepository
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.use_case.user.GetUser
import com.example.weatherapp.domain.use_case.user.InsertUser
import com.example.weatherapp.domain.use_case.user.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val client =  OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(loggingInterceptor)
        }

        return client.connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient/*, gson: Gson*/): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit) = retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepo {
        return DataStoreRepoImpl(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: WeatherDatabase): UserRepository {
        return UserRepositoryImpl(db.userDao)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(db: WeatherDatabase, api: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(db.weatherDao, api)
    }

    @Provides
    @Singleton
    fun provideUserUseCases(repository: UserRepository, dataStoreRepo: DataStoreRepo): UserUseCases {
        return UserUseCases(
            insertUser = InsertUser(repository),
            getUser = GetUser(repository, dataStoreRepo)
        )
    }

}