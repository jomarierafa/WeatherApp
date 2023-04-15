package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.local.User

interface UserRepository {

    suspend fun insertUser(user: User): Long

    suspend fun getUser(username: String, password: String): User?
}