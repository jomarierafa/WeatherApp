package com.jvrcoding.weatherapp.data.util

import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Result
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeCall(apiCall: suspend () -> T): Result<T, DataError.Network> {
    return try {
        val result = apiCall.invoke()
        Result.Success(result)
    } catch (e: IOException) {
        Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: HttpException) {
        val errorCode = e.code()
        val error = mapHttpError(errorCode)
        Result.Error(error)
    } catch (e: Exception) {
        Result.Error(DataError.Network.UNKNOWN)
    }
}

private fun mapHttpError(code: Int): DataError.Network {
    return when (code) {
        400 -> DataError.Network.BAD_REQUEST
        401 -> DataError.Network.UNAUTHORIZED
        403 -> DataError.Network.FORBIDDEN
        404 -> DataError.Network.NOT_FOUND
        408 -> DataError.Network.REQUEST_TIMEOUT
        413 -> DataError.Network.PAYLOAD_TOO_LARGE
        429 -> DataError.Network.TOO_MANY_REQUESTS
        500 -> DataError.Network.SERVER_ERROR
        503 -> DataError.Network.SERVICE_UNAVAILABLE
        else -> DataError.Network.UNKNOWN
    }
}

private fun mapErrorResponse(response: Response<*>?) : String {
    // handle api response here
    return ""
}