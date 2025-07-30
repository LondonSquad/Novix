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
        //Given
        every {
            sharedPreferences.getBoolean("has_onboarding_been_shown", false)
        } returns false
        //When
        val result = service.hasOnboardingBeenShown
        //Then
        assertFalse(result)
        verify { sharedPreferences.getBoolean("has_onboarding_been_shown", false) }
    }

    @Test
    fun `hasOnboardingBeenShown returns true when preference is set`() {
        //Given
        every {
            sharedPreferences.getBoolean("has_onboarding_been_shown", false)
        } returns true

        //When
        val result = service.hasOnboardingBeenShown

        //Then
        assertTrue(result)
        verify { sharedPreferences.getBoolean("has_onboarding_been_shown", false) }
    }

    @Test
    fun `setOnBoardingShown sets preference to true`() {
        //Given
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean("has_onboarding_been_shown", true) } returns editor
        every { editor.apply() } just Runs

        //When
        service.setOnBoardingShown()

        //Then
        verifySequence {
            sharedPreferences.edit()
            editor.putBoolean("has_onboarding_been_shown", true)
            editor.apply()
        }
    }
}
