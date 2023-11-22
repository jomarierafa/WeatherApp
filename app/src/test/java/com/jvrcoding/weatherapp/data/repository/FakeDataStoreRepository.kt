package com.jvrcoding.weatherapp.data.repository

import com.jvrcoding.weatherapp.domain.repository.DataStoreRepo

class FakeDataStoreRepository: DataStoreRepo {

    private var string = "username"

    override suspend fun putString(key: String, value: String) {
        string = value
    }

    override suspend fun getString(key: String): String? {
        return string
    }
}