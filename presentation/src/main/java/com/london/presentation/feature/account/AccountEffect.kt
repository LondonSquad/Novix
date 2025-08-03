package com.london.presentation.feature.account

sealed class AccountEffect {
    data object NavigateToWatchingHistory : AccountEffect()
    data object NavigateToMyRating : AccountEffect()
    data object NavigateToChangePassword : AccountEffect()
    data object ShowContentRestrictionBottomSheet : AccountEffect()
    data object ShowAppearanceBottomSheet : AccountEffect()
    data object ShowLanguageBottomSheet : AccountEffect()
    data object ShowLogoutBottomSheet : AccountEffect()
}