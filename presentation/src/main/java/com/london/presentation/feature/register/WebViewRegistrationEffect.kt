package com.london.presentation.feature.register

sealed class WebViewRegistrationEffect {
    data object NavigateBack : WebViewRegistrationEffect()
    data object RegistrationComplete : WebViewRegistrationEffect()
}