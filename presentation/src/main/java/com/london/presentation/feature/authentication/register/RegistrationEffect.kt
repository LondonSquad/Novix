package com.london.presentation.feature.authentication.register

sealed class RegistrationEffect {
    data object NavigateBack : RegistrationEffect()
    data object RegistrationComplete : RegistrationEffect()
}
