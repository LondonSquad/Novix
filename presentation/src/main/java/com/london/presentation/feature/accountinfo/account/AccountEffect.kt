package com.london.presentation.feature.accountinfo.account

sealed interface AccountEffect {
    data object NavigateToWatchingHistory : AccountEffect
    data object NavigateToMyRating : AccountEffect
    data class NavigateToChangePassword(val url: String) : AccountEffect
    data object NavigateLogout : AccountEffect
}