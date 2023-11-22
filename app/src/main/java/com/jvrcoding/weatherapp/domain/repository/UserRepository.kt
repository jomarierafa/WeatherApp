package com.jvrcoding.weatherapp.domain.repository

import com.jvrcoding.weatherapp.data.local.User

interface UserRepository {

    suspend fun insertUser(user: User): Long

    suspend fun getUser(username: String, password: String): User?
}