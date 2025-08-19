package com.london.data.remote.source.base

import com.london.data.remote.exception.RemoteExceptions
import com.london.data.remote.exception.ResponseException
import com.london.data.remote.model.list.CustomListResponse
import com.london.domain.exception.NetworkException
import kotlinx.coroutines.delay
import retrofit2.Response
import java.net.HttpURLConnection


interface BaseRemoteDatasource {

    suspend fun <T, R> callApi(
        apiCall: suspend () -> Response<T>,
        mapper: (T) -> R
    ): Result<R> = checkIfSuccessful(result = apiCall(), mapper = mapper)

    suspend fun <T, R> callApiWithRetry(
        apiCall: suspend () -> Response<T>,
        mapper: (T) -> R,
        retryCount: Int = 3,
        retryDelayMillis: Long = 2000
    ): Result<R> = retry(
        maxRetries = retryCount,
        delayMillis = retryDelayMillis,
        block = { callApi(apiCall = apiCall, mapper = mapper) })

    private suspend fun <T> retry(
        maxRetries: Int,
        delayMillis: Long, block: suspend () -> T
    ): T {
        var attempt = 0
        var lastError: Throwable? = null

        while (attempt < maxRetries) {
            try {
                return block()
            } catch (e: Exception) {
                if (e.shouldRetry().not()) throw e
                lastError = e
                attempt++
                delay(delayMillis)
            }
        }
        throw lastError ?: Exception("Unknown error occurred")
    }

    fun Exception.shouldRetry(): Boolean = this is RemoteExceptions

    private fun <T, R> checkIfSuccessful(
        result: Response<T>, mapper: (T) -> R
    ): Result<R> {
        if (result.isSuccessful) return getNotEmptyResultOrThrow(result = result, mapper = mapper)
        throw when {
            result.isUnauthorizedError() -> result.toUnAuthorizedException()
            result.isValidationError() -> result.toNetworkValidationException()
            result.isTimeoutError() -> RemoteExceptions.TimeoutException()
            result.isServerError() -> RemoteExceptions.ServerErrorException()
            else -> result.toResponseException()
        }
    }

    private fun <R, T> getNotEmptyResultOrThrow(
        result: Response<T>, mapper: (T) -> R
    ): Result<R> {
        if (result.isEntryNotFoundError()) throw RemoteExceptions.EntryNotFoundException()
        return result.body()?.let { Result.success(mapper(it)) } ?: error("Empty Response")
    }

    private fun Response<*>.isServerError(): Boolean = code() in 500..599
    private fun Response<*>.isUnauthorizedError() = code() == HttpURLConnection.HTTP_UNAUTHORIZED

    private fun Response<*>.isValidationError(): Boolean = code() == 422

    private fun Response<*>.isTimeoutError(): Boolean = when (code()) {

        HttpURLConnection.HTTP_CLIENT_TIMEOUT,
        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> true

        else -> false
    }

    private fun Response<*>.isEntryNotFoundError(): Boolean = (body() as? CustomListResponse)?.let {
        it.statusCode == 21
    } ?: false

    private fun <T> Response<T>.toUnAuthorizedException() = NetworkException.UnAuthorizedException(
        message = errorBody()?.string()
    )

    private fun <T> Response<T>.toNetworkValidationException() =
        NetworkException.ValidationException(message = errorBody()?.string())

    private fun <T> Response<T>.toResponseException() = ResponseException(
        message = errorBody()?.string(),
        code = code()
    )
}
