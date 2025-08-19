package com.london.presentation.shared.base

sealed class ErrorState {
    object UnAuthorized : ErrorState()
    object NoInternet : ErrorState()
    object Timeout : ErrorState()
    object Validation : ErrorState()
    data class RequestFailed(val message: String? = "Request failed") : ErrorState()
}
