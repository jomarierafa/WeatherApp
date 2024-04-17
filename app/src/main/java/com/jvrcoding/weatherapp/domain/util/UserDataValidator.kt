package com.jvrcoding.weatherapp.domain.util

//TODO("move to different package")
class UserDataValidator {
    fun validateLocation(lat: Double?, long: Double?) : Result<Unit, UserDataError> {
        if(lat == null || long == null) {
           return Result.Error(UserDataError.LOCATION_ERROR)
        }
        return Result.Success(Unit)
    }

    enum class UserDataError: Error {
        LOCATION_ERROR
    }
}