package com.example.weatherapp.domain.use_case.user

import com.example.weatherapp.common.hashPassword
import com.example.weatherapp.data.local.InvalidUserException
import com.example.weatherapp.data.local.User
import com.example.weatherapp.domain.repository.UserRepository

class InsertUser(
    private val repository: UserRepository
) {

    suspend operator fun invoke(user: User) {
        if(user.firstname.isBlank()) {
            throw InvalidUserException("Firstname can't be empty.")
        }
        if(user.lastname.isBlank()) {
            throw InvalidUserException("LastName can't be empty.")
        }
        if(user.username.isBlank()) {
            throw InvalidUserException("Username can't be empty.")
        }
        if(user.password.isBlank()) {
            throw InvalidUserException("Password can't be empty.")
        }
        if(user.confirmPassword.isBlank()) {
            throw InvalidUserException("Confirm Password can't be empty.")
        }
        if(user.confirmPassword != user.password) {
            throw InvalidUserException("Password not match.")
        }

        val insertedRow = repository.insertUser(user.copy(password = user.password.hashPassword()))
        if (insertedRow > 0) {
            return
        } else {
            throw InvalidUserException("Username already exists.")
        }

    }

}