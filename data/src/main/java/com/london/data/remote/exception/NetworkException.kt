package com.london.data.remote.exception

sealed class NetworkException(override val message: String?) : Exception(message) {

    data class EmptyResponseException(
        override val message: String?, val status: Int
    ) : NetworkException(message)

    data class UnAuthorizedException(override val message: String?) : NetworkException(message)
    data class ServerErrorException(override val message: String?) : NetworkException(message)
    data class HttpLockedException(override val message: String?) : NetworkException(message)
    data class ValidationException(override val message: String?) : NetworkException(message)
    data class TimeoutException(override val message: String?) : NetworkException(message)
    data class BadRequestException(override val message: String?) : NetworkException(message)
    data class ManyRequestException(override val message: String?) : NetworkException(message)
    data class NoInternetException(
        override val message: String
    ) : NetworkException(message)

    data class UnknownException(
        override val message: String
    ) : NetworkException(message)

}
