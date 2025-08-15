package com.london.data.remote.source.base

import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.list.CustomListResponse
import com.london.domain.exception.EntryNotFoundException
import kotlinx.coroutines.delay
import retrofit2.Response
import java.net.HttpURLConnection

interface BaseRemoteDatasource {

    suspend fun <T, R> callApi(
        apiCall: suspend () -> Response<T>,
        mapper: (T) -> R
    ): Result<R> =
        checkIfSuccessful(result = apiCall(), mapper = mapper)


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

                if (result.body() is CustomListResponse &&
                    (result.body() as CustomListResponse).statusCode == 21
                ) throw result.toEntryNotFoundException()

                getOrEmptyResult(result = result, mapper = mapper).map {
                    it ?: throw NetworkException.EmptyResponseException(
                        "Empty response", result.code()
                    )
                }
            }

            result.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> throw result.toUnAuthorizedException()
            result.code() == HttpURLConnection.HTTP_BAD_REQUEST -> throw result.toBadRequestException()
            result.isServerError() -> throw result.toServerErrorException()
            result.isValidationError() -> throw result.toNetworkValidationException()
            result.isTimeoutError() -> throw result.toTimeoutException()
            else -> throw result.toResponseException()
        }
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

    private fun <T> Response<T>.toUnAuthorizedException() = NetworkException.UnAuthorizedException(
        message = errorBody()?.string(), status = code()
    )


    private fun <T> Response<T>.toServerErrorException() = NetworkException.ServerErrorException(
        message = errorBody()?.string(), status = code()
    )


    private fun <T> Response<T>.toNetworkValidationException() =
        NetworkException.ValidationException(
            message = errorBody()?.string(), status = code()
        )

private fun <T> Response<T>.toResponseException() = NetworkException.EmptyResponseException(
    message = errorBody()?.string(), status = code()
)

private fun <T> Response<T>.toTimeoutException() = NetworkException.TimeoutException(
    message = errorBody()?.string(), status = code()
)

private fun <T> Response<T>.toBadRequestException() = NetworkException.BadRequestException(
    message = errorBody()?.string(), status = code()
)
    private fun <T> Response<T>.toEntryNotFoundException() = EntryNotFoundException()
}
