package com.example.weatherapp.domain.repository

interface DataStoreRepo {

    suspend fun putString(key: String, value: String)

    suspend fun getString(key: String): String?

}