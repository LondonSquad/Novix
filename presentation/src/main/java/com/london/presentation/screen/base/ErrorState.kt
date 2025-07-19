package com.london.presentation.screen.base

sealed class ErrorState(open val message: String? = null) {
    data object NoInternet : ErrorState()
    data class RequestFailed(override val message: String? = null) : ErrorState(message)
}