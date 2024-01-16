package com.jvrcoding.weatherapp.data.repository

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.jvrcoding.weatherapp.BuildConfig
import com.jvrcoding.weatherapp.common.Constant.REMOTE_CONFIG_TAG
import com.jvrcoding.weatherapp.common.DefaultConfigs
import com.jvrcoding.weatherapp.domain.repository.RemoteConfigRepo
import javax.inject.Inject

class RemoteConfigRepoImpl @Inject constructor(): RemoteConfigRepo {

    private val remoteConfig = Firebase.remoteConfig

    override fun initConfigs() {
        /**
         * [cacheInterval] defines the interval of fetches per hour.
         * Use [remoteConfigSettings] to set the minimum fetch interval
         * */
        val cacheInterval = 30L // 30 seconds
        val minFetchInterval: Long = if (BuildConfig.DEBUG) {
            0
        } else {
            cacheInterval
        }

        val configSettings = remoteConfigSettings {
            fetchTimeoutInSeconds = 20L
            minimumFetchIntervalInSeconds = minFetchInterval
        }

        /**
        * Set the default parameters for Remote Config
        * Your app will use these default values until there's a change in the firebase console
        * */
        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(DefaultConfigs.getDefaultParams())
        }

        /**
         * Fetch updates from Firebase console
         * */
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(REMOTE_CONFIG_TAG, "Successful ${it.result}")
                } else {
                    Log.d(REMOTE_CONFIG_TAG, "Failed ${it.exception}")
                }
            }.addOnFailureListener {
                Log.d(REMOTE_CONFIG_TAG,"Exception ${it.message}")
            }
    }

    override fun getConfigs(configKey: String): String {
        initConfigs()

        return remoteConfig.getString(configKey).ifEmpty {
            DefaultConfigs.getDefaultParams()[configKey].toString()
        }
    }

}