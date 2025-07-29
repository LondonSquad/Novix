package com.london.presentation.feature.login.registrasion

sealed class WebViewRegistrationEffect {
    data object NavigateBack : WebViewRegistrationEffect()
    data object RegistrationComplete : WebViewRegistrationEffect()
}