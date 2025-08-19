package com.london.data.local.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.data.utils.LANGUAGE_KEY
import com.london.domain.entity.contentrestriction.ContentRestrictionLevel
import com.london.domain.entity.language.AppLanguage
import com.london.domain.entity.theme.AppTheme
import com.london.domain.repository.AppPreferencesService
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
    private val _isAppDarkMode = MutableStateFlow(getAppTheme())
    override val isAppDarkMode: StateFlow<Boolean> = _isAppDarkMode

    private fun getAppTheme(): Boolean {
        val theme = preferences.getString(
            PreferencesKeys.THEME_KEY, AppTheme.DARK.name
        ) ?: AppTheme.DARK.name
        return theme == AppTheme.DARK.name
    }

    override fun setAppTheme(theme: AppTheme) {
        _isAppDarkMode.value = theme == AppTheme.DARK
        preferences.edit { putString(PreferencesKeys.THEME_KEY, theme.name) }
    }
    //endregion

    //region App Language
    private val _appLanguage = MutableStateFlow(getAppLanguage())
    override val appLanguage: StateFlow<AppLanguage> = _appLanguage

    private fun getAppLanguage(): AppLanguage {
        val languageCode =
            preferences.getString(LANGUAGE_KEY, AppLanguage.ARABIC.code)
        return AppLanguage.fromCode(languageCode ?: AppLanguage.ARABIC.code)
    }

    override fun setAppLanguage(language: AppLanguage) {
        _appLanguage.value = language
        preferences.edit { putString(LANGUAGE_KEY, language.code) }
    }

    override fun setAppLanguageCode(languageCode: String) {
        val language = AppLanguage.fromCode(languageCode)
        setAppLanguage(language)
    }
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
    }

}
