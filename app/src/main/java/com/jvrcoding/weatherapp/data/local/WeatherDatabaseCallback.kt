package com.jvrcoding.weatherapp.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jvrcoding.weatherapp.common.hashPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider

class WeatherDatabaseCallback(
    private val appDb: Provider<WeatherDatabase>
    ) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Initialize the database with first user
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = appDb.get().userDao
            userDao.insertUser(User(
                "Sample",
                "Account",
                "sample",
                "password".hashPassword()
            ))
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)

        // Ensure there is always data in the database
        // This will capture the case of the app storage being cleared
        // This uses the existing instance, so the DB won't leak
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = appDb.get().userDao

            if (userDao.getUserCount() == 0) {
                userDao.insertUser(User(
                    "Sample",
                    "Account",
                    "sample",
                    "password".hashPassword()
                ))
            }
        }
    }
}