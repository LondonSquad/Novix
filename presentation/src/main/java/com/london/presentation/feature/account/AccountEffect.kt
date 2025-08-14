package com.london.presentation.feature.account

sealed interface AccountEffect {
    data object NavigateLogout : AccountEffect
    data object NavigateToMyRating : AccountEffect
    data object NavigateToWatchingHistory : AccountEffect
    data class NavigateToChangePassword(val url: String) : AccountEffect
}