package com.london.data.local.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.domain.AppPreferencesService
import javax.inject.Inject

class AppPreferencesServiceImpl @Inject constructor(
    private val preferences: SharedPreferences,
) : AppPreferencesService {

    override val hasOnboardingBeenShown: Boolean
        get() = preferences.getBoolean(PreferencesKeys.HAS_ONBOARDING_BEEN_SHOWN, false)

    override fun setOnBoardingShown() =
        preferences.edit { putBoolean(PreferencesKeys.HAS_ONBOARDING_BEEN_SHOWN, true) }

    private object PreferencesKeys {
        const val HAS_ONBOARDING_BEEN_SHOWN = "has_onboarding_been_shown"
    }
}