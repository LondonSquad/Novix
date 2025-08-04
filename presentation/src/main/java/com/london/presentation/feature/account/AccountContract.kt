package com.london.presentation.feature.account

import com.london.domain.contentrestriction.ContentRestrictionLevel

interface AccountContract {
    fun onWatchingHistoryClick()
    fun onMyRatingClick()
    fun onContentRestrictionClick()
    fun onChangePasswordClick()
    fun onAppearanceClick()
    fun onLanguageClick()
    fun onUserMenuClick()
    fun onLogoutClick()
    fun onBottomSheetDismiss()
    fun onLoginClick()
    fun onContentRestrictionSave(level: ContentRestrictionLevel)
}