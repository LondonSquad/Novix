package com.london.presentation.feature.account.state

import com.london.domain.theme.AppTheme
import com.london.presentation.feature.base.ErrorState

data class AccountUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val username: String = "",
    val currentAppearance: String = "",
    val currentLanguage: String = "",
    val isUserLoggedIn: Boolean = true,
    val showUserMenu: Boolean = false,
    val showContentRestrictionBottomSheet: Boolean = false,
    // Appearance Bottom Sheet
    val isLightMode: Boolean = false,
    val isDarkMode: Boolean = false,
    val isAppearanceBottomSheetVisible: Boolean = false,
    val appTheme: AppTheme = AppTheme.SYSTEM,

    val isLanguageBottomSheetVisible: Boolean = false,
    val isLogoutBottomSheetVisible: Boolean = false,
)