package com.london.presentation.feature.account

sealed interface AccountEffect {
    data object LogoutNavigation : AccountEffect
    data object MyRatingNavigation : AccountEffect
    data object WatchingHistoryNavigation : AccountEffect
    class ChangePasswordNavigation(val url: String = FORGOT_PASSWORD_URL) : AccountEffect {
        private companion object {
            const val FORGOT_PASSWORD_URL = "https://www.themoviedb.org/reset-password"
        }
    }
}