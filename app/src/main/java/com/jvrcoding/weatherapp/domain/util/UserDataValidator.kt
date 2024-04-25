package com.jvrcoding.weatherapp.domain.util

import com.jvrcoding.weatherapp.domain.model.User

//TODO("move to different package")
class UserDataValidator {

    fun validateUserInputs(user: User): Result<Unit, RegistrationError> {
        if(user.firstname.isBlank()) {
            return Result.Error(RegistrationError.FIRSTNAME_IS_EMPTY)
        }
        if(user.lastname.isBlank()) {
            return Result.Error(RegistrationError.LASTNAME_IS_EMPTY)
        }
        if(user.username.isBlank()) {
            return Result.Error(RegistrationError.USERNAME_IS_EMPTY)
        }
        if(user.password.isBlank()) {
            return Result.Error(RegistrationError.PASSWORD_IS_EMPTY)
        }
        if(user.confirmPassword.isBlank()) {
            return Result.Error(RegistrationError.CONFIRM_PASSWORD_IS_EMPTY)
        }
        if(user.confirmPassword != user.password) {
            return Result.Error(RegistrationError.PASSWORD_NOT_MATCH)
        }

        return Result.Success(Unit)
    }

    fun validateCredential(username: String?, password: String?): Result<Unit, CredentialError> {
        if(username.isNullOrBlank()) {
            return Result.Error(CredentialError.USERNAME_IS_EMPTY)
        }

        if(password.isNullOrBlank()) {
            return Result.Error(CredentialError.PASSWORD_IS_EMPTY)
        }
        return Result.Success(Unit)
    }

    fun validateLocation(lat: Double?, long: Double?) : Result<Unit, LocationError> {
        if(lat == null || long == null) {
           return Result.Error(LocationError.COORDINATES_ERROR)
        }
        return Result.Success(Unit)
    }

    enum class LocationError: Error {
        COORDINATES_ERROR
    }

    enum class CredentialError: Error {
        USERNAME_IS_EMPTY,
        PASSWORD_IS_EMPTY
    }

    enum class RegistrationError: Error {
        FIRSTNAME_IS_EMPTY,
        LASTNAME_IS_EMPTY,
        USERNAME_IS_EMPTY,
        PASSWORD_IS_EMPTY,
        CONFIRM_PASSWORD_IS_EMPTY,
        PASSWORD_NOT_MATCH
    }
}