package com.london.presentation.shared.base

sealed class ErrorState {
    object UnAuthorized : ErrorState()
    object NoInternet : ErrorState()
    object EmptyBody : ErrorState()
    object Timeout : ErrorState()
    object Validation : ErrorState()
    data class RequestFailed(val message: String? = "Request failed") : ErrorState()
    data class EntryNotFound(val message: String? = "Entry not found") : ErrorState()
}

object HttpStatus {
    const val SC_UNAUTHORIZED = 401
    const val SC_TOO_MANY_REQUESTS = 429
}