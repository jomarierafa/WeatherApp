package com.jvrcoding.weatherapp.domain.util


typealias RootError = Error
sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E>(val error: E): Result<Nothing, E>

}

inline fun <T, D, E> Result<D, E>.map(transform: (D) -> T): Result<T, E> =
    when (this) {
        is Result.Error -> this
        is Result.Success ->Result.Success(transform(data))
    }


inline fun <T, D, E: RootError> Result<D, E>.andThen(transform: (D) -> Result<T, E>): Result<T, E> {
    return when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> this
    }
}

inline fun <D, E, T> Result<D, E>.mapError(transform: (E) -> T): Result<D, T> {
    return when (this) {
        is Result.Success -> this
        is Result.Error -> Result.Error(transform(error))
    }
}

inline fun <T, D, E> Result<D, E>.flatMapError(transform: (E) -> Result<D, T>): Result<D, T> {
    return when (this) {
        is Result.Success -> this
        is Result.Error -> transform(this.error)
    }
}


inline fun <D, E : RootError> Result<D, E>.ifSuccess(action: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <D, E : RootError> Result<D, E>.ifError(action: (E) -> Unit): Result<D, E> {
    if (this is Result.Error) {
        action(error)
    }
    return this
}


inline fun <D, E : RootError> Result<D, E>.handleResponse(
    success: (D) -> Result<D, E>,
    err: (E) -> Result<D, E>
): Result<D, E> {
    return when (this) {
        is Result.Success -> {
            success(data)
        }
        is Result.Error -> {
            err(error)
        }
    }
}