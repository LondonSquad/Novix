package com.london.presentation.features.base

sealed class ErrorState(open val message: String? = null) {
    data object NoInternet : ErrorState()
    data class UnAuthorized(override val message: String?) : ErrorState(message)
    data class RequestFailed(override val message: String? = null) : ErrorState(message)
}