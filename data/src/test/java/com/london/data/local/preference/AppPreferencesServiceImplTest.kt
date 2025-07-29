package com.london.data.local.preference

import android.content.SharedPreferences
import com.google.common.base.Verify.verify
import com.london.domain.AppPreferencesService
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.*
import org.junit.Test
import org.junit.Before

class AppPreferencesServiceImplTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var service: AppPreferencesService

    @Before
    fun setUp() {
        sharedPreferences = mockk()
        editor = mockk()
        service = AppPreferencesServiceImpl(sharedPreferences)
    }

    @Test
    fun `hasOnboardingBeenShown returns false when preference is not set`() {
        every {
            sharedPreferences.getBoolean("has_onboarding_been_shown", false)
        } returns false

        val result = service.hasOnboardingBeenShown

        assertFalse(result)
        verify { sharedPreferences.getBoolean("has_onboarding_been_shown", false) }
    }

    @Test
    fun `hasOnboardingBeenShown returns true when preference is set`() {
        every {
            sharedPreferences.getBoolean("has_onboarding_been_shown", false)
        } returns true

        val result = service.hasOnboardingBeenShown

        assertTrue(result)
        verify { sharedPreferences.getBoolean("has_onboarding_been_shown", false) }
    }

    @Test
    fun `setOnBoardingShown sets preference to true`() {
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean("has_onboarding_been_shown", true) } returns editor
        every { editor.apply() } just Runs

        service.setOnBoardingShown()

        verifySequence {
            sharedPreferences.edit()
            editor.putBoolean("has_onboarding_been_shown", true)
            editor.apply()
        }
    }
}
