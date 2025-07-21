package com.london.data.datasource.preferance

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.data.datasource.preferance.AppPreferencesServiceImpl.PreferencesKeys.HAS_ONBOARDING_BEEN_SHOWN
import com.london.domain.AppPreferencesService
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class AppPreferencesServiceImpl(
    @Provided
    private val preferences: SharedPreferences,
) : AppPreferencesService {

    override val hasOnboardingBeenShown: Boolean =
        preferences.getBoolean(HAS_ONBOARDING_BEEN_SHOWN, false)

    override fun setOnBoardingShown() {
        preferences.edit { putBoolean(HAS_ONBOARDING_BEEN_SHOWN, true) }
    }

    private object PreferencesKeys {
        const val HAS_ONBOARDING_BEEN_SHOWN = "has_onboarding_been_shown"
    }
}