package com.london.presentation.feature.account

sealed interface AccountEffect {
    data object NavigateLogout : AccountEffect
    data object NavigateToMyRating : AccountEffect
    data object NavigateToWatchingHistory : AccountEffect
    class NavigateToChangePassword(val url: String = FORGOT_PASSWORD_URL) : AccountEffect {
        private companion object {
            const val FORGOT_PASSWORD_URL = "https://www.themoviedb.org/reset-password"
        }
    }
}