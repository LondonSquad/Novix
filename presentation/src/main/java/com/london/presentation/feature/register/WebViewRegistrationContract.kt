package com.london.presentation.feature.register

interface WebViewRegistrationContract {
    fun onNavigateBack()
    fun onPageLoaded(url: String?)
    fun onUrlChanged(url: String)
    fun shouldInterceptUrl(url: String): Boolean
}