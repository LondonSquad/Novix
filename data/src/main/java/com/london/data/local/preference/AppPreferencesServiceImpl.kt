package com.london.data.local.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.domain.AppPreferencesService
import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.london.domain.contentrestriction.ContentRestrictionLevel
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
        val theme = preferences.getString(PreferencesKeys.THEME_KEY, AppTheme.SYSTEM.name)
        return AppTheme.valueOf(theme ?: AppTheme.SYSTEM.name)
    }

    override fun setAppTheme(theme: AppTheme) =
        preferences.edit { putString(PreferencesKeys.THEME_KEY, theme.name) }
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
    //endregion

    //region Content Restriction
    private val _contentRestrictionLevel = MutableStateFlow(getContentRestrictionLevel())
    override val contentRestrictionLevel: StateFlow<ContentRestrictionLevel> =
        _contentRestrictionLevel

    private fun getContentRestrictionLevel(): ContentRestrictionLevel {
        val level = preferences.getString(
            PreferencesKeys.CONTENT_RESTRICTION_KEY,
            ContentRestrictionLevel.MODERATE.name
        )
        return ContentRestrictionLevel.valueOf(level ?: ContentRestrictionLevel.MODERATE.name)
    }

    override fun setContentRestrictionLevel(level: ContentRestrictionLevel) {
        preferences.edit { putString(PreferencesKeys.CONTENT_RESTRICTION_KEY, level.name) }
        _contentRestrictionLevel.value = level
    }
    //endregion

    private object PreferencesKeys {
        const val HAS_ONBOARDING_BEEN_SHOWN = "has_onboarding_been_shown"
        const val CONTENT_RESTRICTION_KEY = "content_restriction_key"
        const val THEME_KEY = "theme_key"
        const val LANGUAGE_KEY = "language_key"
    }
}