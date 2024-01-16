package com.jvrcoding.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.jvrcoding.weatherapp.BuildConfig
import com.jvrcoding.weatherapp.common.Constant
import com.jvrcoding.weatherapp.data.local.WeatherDatabase
import com.jvrcoding.weatherapp.data.local.WeatherDatabaseCallback
import com.jvrcoding.weatherapp.data.remote.WeatherApi
import com.jvrcoding.weatherapp.data.repository.DataStoreRepoImpl
import com.jvrcoding.weatherapp.data.repository.RemoteConfigRepoImpl
import com.jvrcoding.weatherapp.data.repository.UserRepositoryImpl
import com.jvrcoding.weatherapp.data.repository.WeatherRepositoryImpl
import com.jvrcoding.weatherapp.domain.repository.DataStoreRepo
import com.jvrcoding.weatherapp.domain.repository.RemoteConfigRepo
import com.jvrcoding.weatherapp.domain.repository.UserRepository
import com.jvrcoding.weatherapp.domain.repository.WeatherRepository
import com.jvrcoding.weatherapp.domain.use_case.user.GetUser
import com.jvrcoding.weatherapp.domain.use_case.user.InsertUser
import com.jvrcoding.weatherapp.domain.use_case.user.UserUseCases
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
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context, db: Provider<WeatherDatabase>): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_db"
        )
            .addCallback(WeatherDatabaseCallback(db))
            .build()
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
    fun provideRemoteConfigRepository(remoteConfigRepo: RemoteConfigRepoImpl): RemoteConfigRepo {
//        remoteConfigRepo.initConfigs()
        return RemoteConfigRepoImpl()
    }

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
    fun provideUserUseCases(repository: UserRepository,
                            dataStoreRepo: DataStoreRepo,
                            remoteConfigRepo: RemoteConfigRepoImpl): UserUseCases {
        return UserUseCases(
            insertUser = InsertUser(repository),
            getUser = GetUser(repository, dataStoreRepo, remoteConfigRepo)
        )
    }

}