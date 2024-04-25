package com.jvrcoding.weatherapp.domain.repository

import com.jvrcoding.weatherapp.domain.model.User
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result

interface UserRepository {

    suspend fun insertUser(user: User): Result<Unit, DataError>

    suspend fun getUser(username: String, password: String): Result<User, DataError>
}