package com.london.domain.service

import com.london.domain.entity.contentrestriction.ContentRestrictionLevel
import com.london.domain.entity.language.AppLanguage
import com.london.domain.entity.theme.AppTheme
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
