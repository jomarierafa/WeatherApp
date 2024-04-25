package com.jvrcoding.weatherapp.domain.use_case.user

import com.jvrcoding.weatherapp.domain.model.User
import com.jvrcoding.weatherapp.domain.repository.UserRepository
import com.jvrcoding.weatherapp.domain.util.Error
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.util.UserDataValidator
import com.jvrcoding.weatherapp.domain.util.andThen

class InsertUser(
    private val repository: UserRepository,
    private val userDataValidator: UserDataValidator
) {

    suspend operator fun invoke(user: User): Result<Unit, Error> {
        return userDataValidator.validateUserInputs(user).andThen {
          repository.insertUser(user)
        }
    }

}