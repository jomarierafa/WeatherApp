package com.jvrcoding.weatherapp.data.mapper

import com.jvrcoding.weatherapp.data.local.UserEntity
import com.jvrcoding.weatherapp.domain.model.User

fun UserEntity.toUser() : User {
    return User(
        firstname = firstname,
        lastname = lastname,
        username = lastname,
        password = password,
        confirmPassword = ""
    )
}

fun User.toUserEntity() : UserEntity {
    return UserEntity(
        firstname = firstname,
        lastname = lastname,
        username = lastname,
        password = password
    )
}