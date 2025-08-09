package com.london.presentation.feature.accountinfo.account

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

    // Language Bottom Sheet
    fun onLanguageClick()
    fun onEnglishSelected()
    fun onArabicSelected()
    fun onLanguageSettingsSave()

    // Logout Bottom Sheet
    fun onLogoutConfirmed()

    fun onUserMenuClick()
    fun onLogoutClick()
    fun onBottomSheetDismiss()
    fun onLoginClick()
    fun onContentRestrictionSave(level: ContentRestrictionLevel)
}