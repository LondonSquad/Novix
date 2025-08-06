package com.london.data.local.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.domain.AppPreferencesService
import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme
import com.london.domain.theme.toAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _appTheme = MutableStateFlow(getAppTheme())
    override val appTheme: StateFlow<AppTheme> = _appTheme

    private fun getAppTheme(): AppTheme {
        val theme = preferences.getString(
            PreferencesKeys.THEME_KEY, AppTheme.SYSTEM.name
        ) ?: AppTheme.SYSTEM.name
        return theme.toAppTheme()
    }

    override fun setAppTheme(theme: AppTheme) {
        _appTheme.value = theme.name.toAppTheme()
        preferences.edit { putString(PreferencesKeys.THEME_KEY, theme.name) }
    }
    //endregion

    //region App Language
    private val _appLanguage = MutableStateFlow(getAppLanguage())
    override val appLanguage: StateFlow<AppLanguage> = _appLanguage

    private fun getAppLanguage(): AppLanguage {
        val languageCode =
            preferences.getString(PreferencesKeys.LANGUAGE_KEY, AppLanguage.ENGLISH.code)
        return AppLanguage.fromCode(languageCode ?: AppLanguage.ENGLISH.code)
    }

    override fun setAppLanguage(language: AppLanguage) =
        preferences.edit { putString(PreferencesKeys.LANGUAGE_KEY, language.code) }
    //endregion

    private object PreferencesKeys {
        const val HAS_ONBOARDING_BEEN_SHOWN = "has_onboarding_been_shown"
        const val THEME_KEY = "theme_key"
        const val LANGUAGE_KEY = "language_key"
    }

}