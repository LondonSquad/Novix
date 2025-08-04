package com.london.domain


import com.london.domain.contentrestriction.ContentRestrictionLevel
import kotlinx.coroutines.flow.StateFlow

interface AppPreferencesService {
    //region Onboarding
    val hasOnboardingBeenShown: Boolean
    fun setOnBoardingShown()
    //endregion

    //region Content Restriction
    val contentRestrictionLevel: StateFlow<ContentRestrictionLevel>
    fun setContentRestrictionLevel(level: ContentRestrictionLevel)
    //endregion
}