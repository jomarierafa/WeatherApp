package com.jvrcoding.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, WeatherEntity::class],
    version = 1
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val weatherDao: WeatherDao
}