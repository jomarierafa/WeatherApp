package com.jvrcoding.weatherapp.domain.use_case.user

import com.jvrcoding.weatherapp.common.Constant
import com.jvrcoding.weatherapp.common.DefaultConfigs
import com.jvrcoding.weatherapp.common.hashPassword
import com.jvrcoding.weatherapp.data.local.InvalidUserException
import com.jvrcoding.weatherapp.data.local.User
import com.jvrcoding.weatherapp.domain.repository.DataStoreRepo
import com.jvrcoding.weatherapp.domain.repository.RemoteConfigRepo
import com.jvrcoding.weatherapp.domain.repository.UserRepository

class GetUser(
    private val repository: UserRepository,
    private val dataStoreRepo: DataStoreRepo,
    private val remoteConfigRepo: RemoteConfigRepo
) {

    suspend operator fun invoke(username: String, password: String): User {
        if(username.isBlank()) {
            throw InvalidUserException(remoteConfigRepo.getConfigs(DefaultConfigs.ConfigKeys.MESSAGE))
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