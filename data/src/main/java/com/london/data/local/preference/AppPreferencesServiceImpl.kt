package com.london.data.local.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.domain.AppPreferencesService
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
    }
}