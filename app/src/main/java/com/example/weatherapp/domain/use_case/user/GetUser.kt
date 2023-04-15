package com.example.weatherapp.domain.use_case.user

import com.example.weatherapp.common.Constant
import com.example.weatherapp.common.hashPassword
import com.example.weatherapp.data.local.InvalidUserException
import com.example.weatherapp.data.local.User
import com.example.weatherapp.domain.repository.DataStoreRepo
import com.example.weatherapp.domain.repository.UserRepository

class GetUser(
    private val repository: UserRepository,
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(username: String, password: String): User {
        if(username.isBlank()) {
            throw InvalidUserException("Username can't be empty.")
        }
        if(password.isBlank()) {
            throw InvalidUserException("Password can't be empty.")
        }

        val user = repository.getUser(username, password.hashPassword())
        if(user == null) {
            throw InvalidUserException("Invalid Credential.")
        } else {
            dataStoreRepo.putString(Constant.USERNAME, username)
            return user
        }
    }

}