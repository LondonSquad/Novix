package com.london.presentation.feature.account.state

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
    val showAppearanceBottomSheet: Boolean = false,
    val showLanguageBottomSheet: Boolean = false,
    val showLogoutBottomSheet: Boolean = false,
)