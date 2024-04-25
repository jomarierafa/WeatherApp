package com.jvrcoding.weatherapp.data.local

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jvrcoding.weatherapp.common.hashPassword

@Entity
data class UserEntity(
    val firstname: String,
    val lastname: String,
    @PrimaryKey val username: String,
    val password: String,
    @Ignore val confirmPassword: String // TODO(will remove after test case refactor)
) {
    companion object {
        fun initialData(): UserEntity {
            return UserEntity(
                firstname = "Sample",
                lastname = "Account",
                username = "sample",
                password = "password".hashPassword()
            )
        }
    }

    constructor(firstname: String, lastname: String, username: String, password: String) :
            this(firstname, lastname, username, password, "")
}

class InvalidUserException(message: String): Exception(message)

