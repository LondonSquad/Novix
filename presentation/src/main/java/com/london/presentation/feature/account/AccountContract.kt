package com.london.presentation.feature.account

import com.london.domain.entity.contentrestriction.ContentRestrictionLevel

interface AccountContract {
    fun onMyRatingClick()
    fun onChangePasswordClick()
    fun onWatchingHistoryClick()
    fun onContentRestrictionClick()

    // appearance bottom sheet
    fun onAppearanceClick()
    fun onDarkModeSelected()
    fun onLightModeSelected()
    fun onAppearanceModeSave()
    fun showAppearanceBottomSheet()

    // Language Bottom Sheet
    fun onLanguageClick()
    fun onArabicSelected()
    fun onEnglishSelected()
    fun onLanguageSettingsSave()

    // Logout Bottom Sheet
    fun onLoginClick()
    fun onLogoutClick()
    fun onUserMenuClick()
    fun onLogoutConfirmed()
    fun onBottomSheetDismiss()
    fun onContentRestrictionSave(level: ContentRestrictionLevel)
}
