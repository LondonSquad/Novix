package com.london.presentation.feature.authentication.register

interface RegistrationContract {
    fun onNavigateBack()
    fun onUrlChanged(url: String)
    fun onPageLoaded(url: String?)
    fun shouldInterceptUrl(url: String): Boolean
}