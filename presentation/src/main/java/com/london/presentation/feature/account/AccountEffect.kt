package com.london.presentation.feature.account

sealed interface AccountEffect {
    data object NavigateToWatchingHistory : AccountEffect
    data object NavigateToMyRating : AccountEffect
    data object NavigateToChangePassword : AccountEffect
    data object NavigateToLogin : AccountEffect
    data object ShowContentRestrictionBottomSheet : AccountEffect
    data object ShowAppearanceBottomSheet : AccountEffect
    data object ShowLanguageBottomSheet : AccountEffect
    data object ShowLogoutBottomSheet : AccountEffect
}