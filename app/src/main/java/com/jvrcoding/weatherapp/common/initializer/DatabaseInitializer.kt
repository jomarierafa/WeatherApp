package com.jvrcoding.weatherapp.common.initializer

import android.content.Context
import androidx.startup.Initializer
import com.jvrcoding.weatherapp.data.local.User
import com.jvrcoding.weatherapp.data.local.WeatherDatabase
import com.jvrcoding.weatherapp.di.InitializerEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseInitializer: Initializer<Unit> {

    @Inject lateinit var weatherDatabase: WeatherDatabase

    override fun create(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {

            InitializerEntryPoint.resolve(context).inject(this@DatabaseInitializer)

            val userDao = weatherDatabase.userDao

            if (userDao.getUserCount() == 0) {
                userDao.insertUser(User.initialData())
            }
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }


}