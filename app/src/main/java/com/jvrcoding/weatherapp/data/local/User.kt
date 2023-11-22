package com.jvrcoding.weatherapp.data.local

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class User(
    val firstname: String,
    val lastname: String,
    @PrimaryKey val username: String,
    val password: String,
    @Ignore val confirmPassword: String
) {
    constructor(firstname: String, lastname: String, username: String, password: String) :
            this(firstname, lastname, username, password, "")
}

class InvalidUserException(message: String): Exception(message)

