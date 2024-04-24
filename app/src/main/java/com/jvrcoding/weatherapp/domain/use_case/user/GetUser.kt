package com.jvrcoding.weatherapp.domain.use_case.user

import com.jvrcoding.weatherapp.common.Constant
import com.jvrcoding.weatherapp.common.DefaultConfigs
import com.jvrcoding.weatherapp.data.local.User
import com.jvrcoding.weatherapp.domain.repository.DataStoreRepo
import com.jvrcoding.weatherapp.domain.repository.RemoteConfigRepo
import com.jvrcoding.weatherapp.domain.repository.UserRepository
import com.jvrcoding.weatherapp.domain.util.Error
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.util.UserDataValidator
import com.jvrcoding.weatherapp.domain.util.andThen
import com.jvrcoding.weatherapp.domain.util.mapError

class GetUser(
    private val repository: UserRepository,
    private val dataStoreRepo: DataStoreRepo,
    private val remoteConfigRepo: RemoteConfigRepo,
    private val userDataValidator: UserDataValidator
) {

    suspend operator fun invoke(username: String, password: String): Result<User, Error> {
        return userDataValidator.validateCredential(username, password)
            .andThen { repository.getUser(username, password) }
            .andThen {
                dataStoreRepo.putString(Constant.USERNAME, username)
                Result.Success(it)
            }
            .mapError {
                val errorMessage =
                    remoteConfigRepo.getConfigs(DefaultConfigs.ConfigKeys.MESSAGE)
                it
            }
    }

}
