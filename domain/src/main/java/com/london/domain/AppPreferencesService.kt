package com.london.domain

import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme

interface AppPreferencesService {
    //region Onboarding
    val hasOnboardingBeenShown: Boolean
    fun setOnBoardingShown()

    //endregion
    //region Theme
    val appTheme: String
    fun getAppTheme(): AppTheme
    fun setAppTheme(theme: AppTheme)

    //endregion
    //region Language
    val appLanguageCode: String
    fun getAppLanguage(): AppLanguage
    fun setAppLanguage(language: AppLanguage)
    //endregion
}