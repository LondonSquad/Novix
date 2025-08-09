package com.london.presentation.feature.accountinfo.account

import com.london.domain.contentrestriction.ContentRestrictionLevel
import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme
import com.london.presentation.shared.base.ErrorState

data class AccountUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val userName: String = "",
    val userAvatar: String? = null,
    val currentAppearance: String = "",
    val currentLanguage: String = "",
    val currentContentRestriction: ContentRestrictionLevel = ContentRestrictionLevel.MODERATE,
    val isUserLoggedIn: Boolean = false,
    val showUserMenu: Boolean = false,
    val isLightMode: Boolean = false,
    val isDarkMode: Boolean = false,
    val appTheme: AppTheme = AppTheme.DARK,
    val isLogoutLoading: Boolean = false,
    val appLanguage: AppLanguage = AppLanguage.ARABIC,
    val activeBottomSheet: ActiveBottomSheet = ActiveBottomSheet.None
)

sealed interface ActiveBottomSheet {
    data object None : ActiveBottomSheet
    data object Appearance : ActiveBottomSheet
    data object Language : ActiveBottomSheet
    data object Logout : ActiveBottomSheet
    data object ContentRestriction : ActiveBottomSheet
}