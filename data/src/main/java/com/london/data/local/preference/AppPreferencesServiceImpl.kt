package com.london.data.local.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.domain.AppPreferencesService
import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme
import javax.inject.Inject

class AppPreferencesServiceImpl @Inject constructor(
    private val preferences: SharedPreferences
) : AppPreferencesService {

    //region Onboarding
    override val hasOnboardingBeenShown: Boolean
        get() = preferences.getBoolean(PreferencesKeys.HAS_ONBOARDING_BEEN_SHOWN, false)

    override fun setOnBoardingShown() =
        preferences.edit { putBoolean(PreferencesKeys.HAS_ONBOARDING_BEEN_SHOWN, true) }
    //endregion

    //region App Theme
    override val appTheme: String?
        get() = preferences.getString(PreferencesKeys.THEME_KEY, AppTheme.SYSTEM.name)

    override fun getAppTheme(): AppTheme =
        AppTheme.valueOf(appTheme ?: AppTheme.SYSTEM.name)

    override fun setAppTheme(theme: AppTheme) =
        preferences.edit { putString(PreferencesKeys.THEME_KEY, theme.name) }
    //endregion

    //region App Language
    override val appLanguage: String?
        get() = preferences.getString(PreferencesKeys.LANGUAGE_KEY, AppLanguage.ENGLISH.name)

    override fun getAppLanguage(): AppLanguage =
        AppLanguage.valueOf(appLanguage ?: AppLanguage.ENGLISH.name)

    override fun setAppLanguage(language: AppLanguage) =
        preferences.edit { putString(PreferencesKeys.LANGUAGE_KEY, language.name) }
    //endregion

    private object PreferencesKeys {
        const val HAS_ONBOARDING_BEEN_SHOWN = "has_onboarding_been_shown"
        const val THEME_KEY = "theme_key"
        const val LANGUAGE_KEY = "language_key"
    }
}