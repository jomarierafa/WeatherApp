package com.jvrcoding.weatherapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jvrcoding.weatherapp.common.Constant.DATASTORE_NAME
import com.jvrcoding.weatherapp.domain.repository.DataStoreRepo
import kotlinx.coroutines.flow.first

val Context.dataStore : DataStore<Preferences> by  preferencesDataStore(name = DATASTORE_NAME)

class DataStoreRepoImpl(
    private val context: Context
) : DataStoreRepo {

    override suspend fun putString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[preferencesKey]
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}