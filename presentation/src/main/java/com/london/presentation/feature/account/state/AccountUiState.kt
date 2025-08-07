package com.london.presentation.feature.account.state

import com.london.domain.contentrestriction.ContentRestrictionLevel
import com.london.domain.theme.AppTheme
import com.london.presentation.shared.base.ErrorState

data class AccountUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val username: String = "",
    val currentAppearance: String = "",
    val currentLanguage: String = "",
    val currentContentRestriction: ContentRestrictionLevel = ContentRestrictionLevel.MODERATE,
    val isUserLoggedIn: Boolean = false,
    val showUserMenu: Boolean = false,
    val showContentRestrictionBottomSheet: Boolean = false,
    val isLightMode: Boolean = false,
    val isDarkMode: Boolean = false,
    val isAppearanceBottomSheetVisible: Boolean = false,
    val appTheme: AppTheme = AppTheme.DARK,
    val isLogoutBottomSheetVisible: Boolean = false,
    val isLogoutLoading: Boolean = false,
    val isLanguageBottomSheetVisible: Boolean = false,
)