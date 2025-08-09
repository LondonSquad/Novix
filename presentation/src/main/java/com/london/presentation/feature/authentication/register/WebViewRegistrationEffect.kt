package com.london.presentation.feature.authentication.register

sealed class WebViewRegistrationEffect {
    data object NavigateBack : WebViewRegistrationEffect()
    data object RegistrationComplete : WebViewRegistrationEffect()
}