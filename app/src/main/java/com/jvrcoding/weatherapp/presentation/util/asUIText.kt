package com.jvrcoding.weatherapp.presentation.util

import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Error
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.util.UserDataValidator.LocationError
import com.jvrcoding.weatherapp.domain.util.UserDataValidator.CredentialError
import com.jvrcoding.weatherapp.domain.util.UserDataValidator.RegistrationError

fun Error.asUiText(): UiText {
    return when (this) {
        is DataError.Local -> asDataErrorUiText()
        is DataError.Network -> asDataErrorUiText()
        is CredentialError -> asCredentialErrorUiText()
        is LocationError -> asLocationErrorUiText()
        is RegistrationError -> asRegistrationErrorUiText()
        else ->  UiText.StringResource(R.string.unknown_error)
    }
}

private fun DataError.Local.asDataErrorUiText(): UiText {
    return when (this) {
        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.error_disk_full
        )
        DataError.Local.NO_DATA_FOUND -> UiText.StringResource(
            R.string.invalid_credentials
        )

        DataError.Local.USERNAME_ALREADY_EXIST -> UiText.StringResource(
            R.string.username_already_exist
        )
    }
}

private fun DataError.Network.asDataErrorUiText(): UiText {
    return when (this) {

        DataError.Network.BAD_REQUEST -> UiText.StringResource(
            R.string.bad_request
        )

        DataError.Network.UNAUTHORIZED -> UiText.StringResource(
            R.string.unauthorized_error
        )

        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(
            R.string.youve_hit_your_rate_limit
        )

        DataError.Network.SERIALIZATION -> UiText.StringResource(
            R.string.error_serialization
        )

        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet
        )

        DataError.Network.TOO_MANY_REQUESTS,
        DataError.Network.REQUEST_TIMEOUT,
        DataError.Network.SERVICE_UNAVAILABLE,
        DataError.Network.SERVER_ERROR,
        DataError.Network.FORBIDDEN,
        DataError.Network.NOT_FOUND -> UiText.StringResource(
            R.string.something_went_wrong_error
        )

        DataError.Network.UNKNOWN -> UiText.StringResource(
            R.string.unknown_error
        )
    }
}

private fun CredentialError.asCredentialErrorUiText(): UiText {
    return when (this) {
        CredentialError.USERNAME_IS_EMPTY -> UiText.StringResource(
            R.string.username_is_empty
        )
        CredentialError.PASSWORD_IS_EMPTY -> UiText.StringResource(
            R.string.password_is_empty
        )
    }
}

private fun LocationError.asLocationErrorUiText(): UiText {
    return when (this) {
        LocationError.COORDINATES_ERROR -> UiText.StringResource(
            R.string.location_error
        )
    }
}

fun RegistrationError.asRegistrationErrorUiText(): UiText {
    return when (this) {
        RegistrationError.FIRSTNAME_IS_EMPTY -> UiText.StringResource(
            R.string.firstname_is_empty
        )

        RegistrationError.LASTNAME_IS_EMPTY -> UiText.StringResource(
            R.string.lastname_is_empty
        )

        RegistrationError.USERNAME_IS_EMPTY -> UiText.StringResource(
            R.string.username_is_empty
        )

        RegistrationError.PASSWORD_IS_EMPTY -> UiText.StringResource(
            R.string.password_is_empty
        )

        RegistrationError.CONFIRM_PASSWORD_IS_EMPTY -> UiText.StringResource(
            R.string.confirm_password_is_empty
        )

        RegistrationError.PASSWORD_NOT_MATCH -> UiText.StringResource(
            R.string.password_not_match
        )
    }
}

fun Result.Error<DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}