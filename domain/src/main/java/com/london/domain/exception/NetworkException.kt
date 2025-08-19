package com.london.domain.exception

sealed class NetworkException(override val message: String?) : Exception(message) {

    data class NoInternetException(override val message: String?) : NetworkException(message)

    data class UnAuthorizedException(override val message: String?) : NetworkException(message)

    data class ValidationException(override val message: String?) : NetworkException(message)
}
