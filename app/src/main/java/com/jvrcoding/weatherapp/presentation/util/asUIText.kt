package com.jvrcoding.weatherapp.presentation.util

import com.jvrcoding.weatherapp.R
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Error
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.util.UserDataValidator.UserDataError

fun Error.asUiText(): UiText {
    return when (this) {
        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.error_disk_full
        )
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
        DataError.Network.NOT_FOUND   -> UiText.StringResource(
            R.string.something_went_wrong_error
        )
        DataError.Network.UNKNOWN -> UiText.StringResource(
            R.string.unknown_error
        )
        UserDataError.LOCATION_ERROR -> UiText.StringResource(
            R.string.location_error
        )
    }
}

fun Result.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}