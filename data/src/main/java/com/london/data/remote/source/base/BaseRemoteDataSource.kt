package com.london.data.remote.source.base

import com.london.data.remote.exception.NetworkException
import com.london.data.remote.exception.UnProcessableEntityException
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.net.HttpURLConnection

interface BaseRemoteDatasource {

    suspend fun <T, R> callApi(
        apiCall: suspend () -> Response<T>, mapper: (T) -> R
    ): Result<R> = checkIfSuccessful(result = apiCall(), mapper = mapper)

    suspend fun <T, R> callApiWithRetry(
        apiCall: suspend () -> Response<T>,
        mapper: (T) -> R,
        retryCount: Int = 3,
        retryDelayMillis: Long = 1000
    ): Result<R> = retry(
        maxRetries = retryCount,
        delayMillis = retryDelayMillis,
        block = { callApi(apiCall = apiCall, mapper = mapper) })

    private suspend fun <T> retry(
        maxRetries: Int, delayMillis: Long, block: suspend () -> T
    ): T {
        var attempt = 0
        var lastError: Throwable? = null

        while (attempt < maxRetries) {
            try {
                return block()
            } catch (e: Exception) {
                lastError = e
                attempt++
                delay(delayMillis)
            }
        }
        throw lastError ?: Exception("Unknown error occurred")
    }

    private fun <T, R> checkIfSuccessful(
        result: Response<T>, mapper: (T) -> R
    ): Result<R> {
        return when {
            result.isSuccessful -> {
                getOrEmptyResult(result = result, mapper = mapper).map {
                    it ?: throw NetworkException.EmptyResponseException(
                        "Empty response", result.code()
                    )
                }
            }

            result.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> throw result.toUnAuthorizedException()
            result.code() == HttpURLConnection.HTTP_BAD_REQUEST -> throw result.toBadRequestException()
            result.code() == TOO_MANY_REQUESTS -> throw result.toManyRequestException()
            result.isServerError() -> throw result.toServerErrorException()
            result.isValidationError() -> throw result.toNetworkValidationException()
            result.isTimeoutError() -> throw result.toTimeoutException()
            result.code() == HTTP_LOCKED -> throw result.toHttpLockedException()
            else -> throw result.toResponseException()
        }
    }

    companion object {
        const val TOO_MANY_REQUESTS = 429
        const val HTTP_LOCKED = 423
    }

    private fun <R, T> getOrEmptyResult(
        result: Response<T>, mapper: (T) -> R
    ): Result<R?> = result.body()?.let {
        Result.success(mapper(it))
    } ?: Result.success(null)

    private fun Response<*>.isServerError(): Boolean = code() in 500..599

    private fun Response<*>.isValidationError(): Boolean = code() == 422

    private fun Response<*>.isTimeoutError(): Boolean = when (code()) {
        HttpURLConnection.HTTP_CLIENT_TIMEOUT, HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> true

        else -> false
    }

    private fun <T> Response<T>.toUnAuthorizedException(): NetworkException.UnAuthorizedException {
        val errorBody = errorBody()?.string() ?: ""
        val exception = runCatching {
            Json.decodeFromString<NetworkException.UnAuthorizedException>(errorBody)
        }.getOrNull()
        return NetworkException.UnAuthorizedException(
            message = exception?.message ?: errorBody
        )
    }

    private fun <T> Response<T>.toServerErrorException() = NetworkException.ServerErrorException(
        message = errorBody()?.string()
    )

    private fun <T> Response<T>.toHttpLockedException() = NetworkException.HttpLockedException(
        message = errorBody()?.string()
    )

    private fun <T> Response<T>.toNetworkValidationException(): NetworkException.ValidationException {
        val errorBody = errorBody()?.string() ?: ""
        val exception = runCatching {
            Json.decodeFromString<UnProcessableEntityException>(errorBody)
        }.getOrNull()
        return NetworkException.ValidationException(
            message = exception?.message ?: errorBody
        )
    }

    private fun <T> Response<T>.toResponseException() = NetworkException.EmptyResponseException(
        message = errorBody()?.string(), status = code()
    )

    private fun <T> Response<T>.toTimeoutException() = NetworkException.TimeoutException(
        message = errorBody()?.string()
    )

    private fun <T> Response<T>.toBadRequestException() = NetworkException.BadRequestException(
        message = errorBody()?.string()
    )

    private fun <T> Response<T>.toManyRequestException() = NetworkException.ManyRequestException(
        message = errorBody()?.string()
    )
}
