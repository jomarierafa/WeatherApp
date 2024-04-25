package com.jvrcoding.weatherapp.data.repository

import com.jvrcoding.weatherapp.common.hashPassword
import com.jvrcoding.weatherapp.domain.model.User
import com.jvrcoding.weatherapp.domain.repository.UserRepository
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result

class FakeUserRepository: UserRepository {

    private val userList = mutableListOf<User>()
    override suspend fun insertUser(user: User): Result<Unit, DataError> {
        return if(userList.add(user)) {
            Result.Success(Unit)
        } else {
            Result.Error(DataError.Local.USERNAME_ALREADY_EXIST)
        }
    }

    override suspend fun getUser(username: String, password: String): Result<User, DataError> {
        val user = userList.find { it.username == username && it.password == password.hashPassword() }
        return if (user != null) {
            Result.Success(user)
        } else {
            Result.Error(DataError.Local.NO_DATA_FOUND)
        }

    }

}