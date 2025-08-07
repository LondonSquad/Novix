package com.london.data.local.preference

import android.content.SharedPreferences
import com.london.domain.AppPreferencesService
import com.london.domain.contentrestriction.ContentRestrictionLevel
import com.london.domain.language.AppLanguage
import com.london.domain.theme.AppTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppPreferencesServiceImplTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var service: AppPreferencesService

    companion object {
        private const val ONBOARDING_KEY = "has_onboarding_been_shown"
        private const val THEME_KEY = "theme_key"
        private const val LANGUAGE_KEY = "language_key"
        private const val CONTENT_RESTRICTION_KEY = "content_restriction_key"
    }

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        sharedPreferences = mockk()
        editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor

        every {
            sharedPreferences.getString(THEME_KEY, AppTheme.SYSTEM.name)
        } returns AppTheme.SYSTEM.name

        every {
            sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ENGLISH.code)
        } returns AppLanguage.ENGLISH.code

        every {
            sharedPreferences.getString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.MODERATE.name)
        } returns ContentRestrictionLevel.MODERATE.name

        service = AppPreferencesServiceImpl(sharedPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `appTheme returns default SYSTEM when no preference is set`() = runTest {
        // Given
        every { sharedPreferences.getString(THEME_KEY, AppTheme.SYSTEM.name) } returns null

        // When
        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentTheme = service.isAppDarkMode.first()

        // Then
        assertEquals(AppTheme.SYSTEM, currentTheme)
    }

    @Test
    fun `appTheme returns specific theme when preference is set`() = runTest {
        // Given
        every { sharedPreferences.getString(THEME_KEY, AppTheme.SYSTEM.name) } returns AppTheme.DARK.name

        // When
        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentTheme = service.isAppDarkMode.first()

        // Then
        assertEquals(AppTheme.DARK, currentTheme)
        verify { sharedPreferences.getString(THEME_KEY, AppTheme.SYSTEM.name) }
    }

    @Test
    fun `appLanguage returns default ENGLISH when no preference is set`() = runTest {
        // Given
        every { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ENGLISH.code) } returns null

        // When
        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLanguage = service.appLanguage.first()

        // Then
        assertEquals(AppLanguage.ENGLISH, currentLanguage)
    }

    @Test
    fun `appLanguage returns specific language when preference is set`() = runTest {
        // Given
        every { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ENGLISH.code) } returns AppLanguage.ARABIC.code

        // When
        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLanguage = service.appLanguage.first()

        // Then
        assertEquals(AppLanguage.ARABIC, currentLanguage)
        verify { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ENGLISH.code) }
    }

    @Test
    fun `appLanguage returns default language when saved code is invalid and fromCode defaults`() = runTest {
        // Given
        val invalidCode = "xx"
        every { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ENGLISH.code) } returns invalidCode

        // When
        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLanguage = service.appLanguage.first()

        // Then
        assertEquals(AppLanguage.ENGLISH, currentLanguage)
        verify { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ENGLISH.code) }
    }

    @Test
    fun `hasOnboardingBeenShown returns false when preference is not set`() {
        // Given
        every { sharedPreferences.getBoolean(ONBOARDING_KEY, false) } returns false

        // When
        val result = service.hasOnboardingBeenShown

        // Then
        assertFalse(result)
        verify { sharedPreferences.getBoolean(ONBOARDING_KEY, false) }
    }

    @Test
    fun `hasOnboardingBeenShown returns true when preference is set`() {
        // Given
        every { sharedPreferences.getBoolean(ONBOARDING_KEY, false) } returns true

        // When
        val result = service.hasOnboardingBeenShown

        // Then
        assertTrue(result)
        verify { sharedPreferences.getBoolean(ONBOARDING_KEY, false) }
    }

    @Test
    fun `setOnBoardingShown sets preference to true`() = runTest {
        // Given
        every { editor.putBoolean(ONBOARDING_KEY, true) } returns editor

        // When
        service.setOnBoardingShown()

        // Then
        verifyOrder {
            sharedPreferences.edit()
            editor.putBoolean(ONBOARDING_KEY, true)
        }
        verify(exactly = 1) { editor.apply() }
    }

    @Test
    fun `contentRestrictionLevel returns default MODERATE when no preference is set`() = runTest {
        // Given
        every { sharedPreferences.getString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.MODERATE.name) } returns null

        // When
        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLevel = service.contentRestrictionLevel.first()

        // Then
        assertEquals(ContentRestrictionLevel.MODERATE, currentLevel)
    }

    @Test
    fun `contentRestrictionLevel returns specific level when preference is set`() = runTest {
        // Given
        every { sharedPreferences.getString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.MODERATE.name) } returns ContentRestrictionLevel.STRICT.name

        // When
        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLevel = service.contentRestrictionLevel.first()

        // Then
        assertEquals(ContentRestrictionLevel.STRICT, currentLevel)
    }

    @Test
    fun `setContentRestrictionLevel saves preference and updates state`() = runTest {
        // Given
        every { editor.putString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.OFF.name) } returns editor

        // When
        service.setContentRestrictionLevel(ContentRestrictionLevel.OFF)

        // Then
        verifyOrder {
            sharedPreferences.edit()
            editor.putString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.OFF.name)
            editor.apply()
        }

        val updatedLevel = service.contentRestrictionLevel.first()
        assertEquals(ContentRestrictionLevel.OFF, updatedLevel)
    }

}