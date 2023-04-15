package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.User
import com.example.weatherapp.data.local.UserDao
import com.example.weatherapp.domain.repository.UserRepository

class UserRepositoryImpl(
    private val dao: UserDao
): UserRepository {

    override suspend fun insertUser(user: User): Long {
        return dao.insertUser(user)
    }

    override suspend fun getUser(username: String, password: String): User? {
        return dao.getUser(username, password)
    }


}