package com.london.domain

import com.london.domain.contentrestriction.ContentRestrictionLevel
import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow

interface AppPreferencesService {
    val hasOnboardingBeenShown: Boolean
    fun setOnBoardingShown()

    val isAppDarkMode: StateFlow<Boolean>
    fun setAppTheme(theme: AppTheme)

    val appLanguage: StateFlow<AppLanguage>
    fun setAppLanguage(language: AppLanguage)
    fun setAppLanguageCode(languageCode: String)

    val contentRestrictionLevel: StateFlow<ContentRestrictionLevel>
    fun setContentRestrictionLevel(level: ContentRestrictionLevel)
}