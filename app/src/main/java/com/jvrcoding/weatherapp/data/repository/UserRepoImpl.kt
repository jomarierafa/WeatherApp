package com.jvrcoding.weatherapp.data.repository

import com.jvrcoding.weatherapp.common.hashPassword
import com.jvrcoding.weatherapp.data.local.UserDao
import com.jvrcoding.weatherapp.data.mapper.toUser
import com.jvrcoding.weatherapp.data.mapper.toUserEntity
import com.jvrcoding.weatherapp.domain.model.User
import com.jvrcoding.weatherapp.domain.repository.UserRepository
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result

class UserRepoImpl(
    private val dao: UserDao
) : UserRepository {

    override suspend fun insertUser(user: User): Result<Unit, DataError> {
        val result = dao.insertUser(
            user.copy(password = user.password.hashPassword()).toUserEntity()
        )
        return if (result > 0) {
            Result.Success(Unit)
        } else {
            Result.Error(DataError.Local.USERNAME_ALREADY_EXIST)
        }
    }

    override suspend fun getUser(username: String, password: String): Result<User, DataError> {
        val user = dao.getUser(username, password.hashPassword())
            ?: return Result.Error(DataError.Local.NO_DATA_FOUND)
        return Result.Success(user.toUser())
    }


}