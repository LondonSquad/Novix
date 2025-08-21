package com.london.presentation.feature.account

import com.london.domain.entity.contentrestriction.ContentRestrictionLevel
import com.london.domain.entity.language.AppLanguage
import com.london.domain.entity.theme.AppTheme
import com.london.presentation.shared.base.ErrorState

data class AccountUiState(
    val userName: String = "",
    val error: ErrorState? = null,
    val userAvatar: String? = null,
    val isLoading: Boolean = false,
    val currentLanguage: String = "",
    val showUserMenu: Boolean = false,
    val currentAppearance: String = "",
    val isUserLoggedIn: Boolean = false,
    val isLogoutLoading: Boolean = false,
    val appTheme: AppTheme = AppTheme.DARK,
    val appLanguage: AppLanguage = AppLanguage.ARABIC,
    val activeBottomSheet: ActiveBottomSheet = ActiveBottomSheet.None,
    val currentContentRestriction: ContentRestrictionLevel = ContentRestrictionLevel.MODERATE
)

sealed interface ActiveBottomSheet {
    data object None : ActiveBottomSheet
    data object Logout : ActiveBottomSheet
    data object Language : ActiveBottomSheet
    data object Appearance : ActiveBottomSheet
    data object ContentRestriction : ActiveBottomSheet
}
