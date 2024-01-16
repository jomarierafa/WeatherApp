package com.jvrcoding.weatherapp.data.repository

import com.jvrcoding.weatherapp.common.DefaultConfigs
import com.jvrcoding.weatherapp.domain.repository.RemoteConfigRepo

class FakeRemoteConfigRepository: RemoteConfigRepo {

    override fun initConfigs() {

    }

    override fun getConfigs(configKey: String): String {
        return DefaultConfigs.getDefaultParams()[configKey].toString()
    }
}