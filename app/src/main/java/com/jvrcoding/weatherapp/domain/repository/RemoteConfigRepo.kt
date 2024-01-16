package com.jvrcoding.weatherapp.domain.repository

interface RemoteConfigRepo {

    fun initConfigs()

    fun getConfigs(configKey: String): String
}