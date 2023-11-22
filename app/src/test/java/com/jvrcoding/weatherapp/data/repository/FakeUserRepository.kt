package com.jvrcoding.weatherapp.data.repository

import com.jvrcoding.weatherapp.data.local.User
import com.jvrcoding.weatherapp.domain.repository.UserRepository

class FakeUserRepository: UserRepository {

    private val userList = mutableListOf<User>()

    override suspend fun insertUser(user: User): Long {
        return if(userList.add(user)) 1 else 0
    }

    override suspend fun getUser(username: String, password: String): User? {
        return userList.find { it.username == username && it.password == password }
    }
}