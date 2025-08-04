package com.london.domain

import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow

interface AppPreferencesService {
    //region Onboarding
    val hasOnboardingBeenShown: Boolean
    fun setOnBoardingShown()
    //endregion
    //region Theme
    val appTheme: StateFlow<AppTheme>
    fun setAppTheme(theme: AppTheme)
    //endregion
    //region Language
    val appLanguage: StateFlow<AppLanguage>
    fun setAppLanguage(language: AppLanguage)
    //endregion
}