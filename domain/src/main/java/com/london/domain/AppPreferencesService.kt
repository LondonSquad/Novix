package com.london.domain

interface AppPreferencesService {
    val hasOnboardingBeenShown: Boolean
    fun setOnBoardingShown()
}