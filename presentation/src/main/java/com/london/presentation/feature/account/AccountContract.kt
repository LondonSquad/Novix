package com.london.presentation.feature.account

import com.london.domain.contentrestriction.ContentRestrictionLevel

interface AccountContract {
    fun onWatchingHistoryClick()
    fun onMyRatingClick()
    fun onContentRestrictionClick()
    fun onChangePasswordClick()
    // appearance bottom sheet
    fun onAppearanceClick()
    fun onDarkModeSelected()
    fun onLightModeSelected()
    fun onAppearanceModeSave()
    fun showAppearanceBottomSheet()

    // logout bottom sheet
    fun onLogoutConfirmed()

    fun onLanguageClick()
    fun onUserMenuClick()
    fun onLogoutClick()
    fun onBottomSheetDismiss()
    fun onLoginClick()
    fun onContentRestrictionSave(level: ContentRestrictionLevel)
}