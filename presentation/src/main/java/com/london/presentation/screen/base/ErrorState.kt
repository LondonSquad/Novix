package com.london.presentation.screen.base

sealed class ErrorState {
    object UnAuthorized : ErrorState()
    object NoInternet : ErrorState()
    object EmptyBody : ErrorState()
    object Timeout : ErrorState()
    object Validation : ErrorState()
    data class RequestFailed(val message: String? = "Request failed") : ErrorState()
}

object HttpStatus {
    const val SC_UNAUTHORIZED = 401
    const val SC_TOO_MANY_REQUESTS = 429
}