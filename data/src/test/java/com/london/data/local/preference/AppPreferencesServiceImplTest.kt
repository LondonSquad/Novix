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
            sharedPreferences.getString(THEME_KEY, AppTheme.DARK.name)
        } returns AppTheme.DARK.name

        every {
            sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ARABIC.code)
        } returns AppLanguage.ARABIC.code

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
    fun `appTheme returns default Dark theme true when no preference is set`() = runTest {
        every { sharedPreferences.getString(THEME_KEY, AppTheme.DARK.name) } returns null

        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentTheme = service.isAppDarkMode.first()

        assertEquals(true, currentTheme)
    }

    @Test
    fun `appTheme returns specific theme when preference is set`() = runTest {
        every { sharedPreferences.getString(THEME_KEY, AppTheme.DARK.name) } returns AppTheme.DARK.name

        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentTheme = service.isAppDarkMode.first()

        assertEquals(true, currentTheme)
        verify { sharedPreferences.getString(THEME_KEY, AppTheme.DARK.name) }
    }

    @Test
    fun `appLanguage returns default ARABIC when no preference is set`() = runTest {
        every { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ARABIC.code) } returns null

        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLanguage = service.appLanguage.first()

        assertEquals(AppLanguage.ARABIC, currentLanguage)
    }

    @Test
    fun `appLanguage returns specific language when preference is set`() = runTest {
        every { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ARABIC.code) } returns AppLanguage.ENGLISH.code

        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLanguage = service.appLanguage.first()

        assertEquals(AppLanguage.ENGLISH, currentLanguage)
        verify { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ARABIC.code) }
    }

    @Test
    fun `appLanguage returns default language when saved code is invalid and fromCode defaults`() = runTest {
        val invalidCode = "xx"
        every { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ARABIC.code) } returns invalidCode

        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLanguage = service.appLanguage.first()

        assertEquals(AppLanguage.ARABIC, currentLanguage)
        verify { sharedPreferences.getString(LANGUAGE_KEY, AppLanguage.ARABIC.code) }
    }

    @Test
    fun `hasOnboardingBeenShown returns false when preference is not set`() {
        every { sharedPreferences.getBoolean(ONBOARDING_KEY, false) } returns false

        val result = service.hasOnboardingBeenShown

        assertFalse(result)
        verify { sharedPreferences.getBoolean(ONBOARDING_KEY, false) }
    }

    @Test
    fun `hasOnboardingBeenShown returns true when preference is set`() {
        every { sharedPreferences.getBoolean(ONBOARDING_KEY, false) } returns true

        val result = service.hasOnboardingBeenShown

        assertTrue(result)
        verify { sharedPreferences.getBoolean(ONBOARDING_KEY, false) }
    }

    @Test
    fun `setOnBoardingShown sets preference to true`() = runTest {
        every { editor.putBoolean(ONBOARDING_KEY, true) } returns editor

        service.setOnBoardingShown()

        verifyOrder {
            sharedPreferences.edit()
            editor.putBoolean(ONBOARDING_KEY, true)
        }
        verify(exactly = 1) { editor.apply() }
    }

    @Test
    fun `contentRestrictionLevel returns default MODERATE when no preference is set`() = runTest {
        every { sharedPreferences.getString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.MODERATE.name) } returns null

        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLevel = service.contentRestrictionLevel.first()

        assertEquals(ContentRestrictionLevel.MODERATE, currentLevel)
    }

    @Test
    fun `contentRestrictionLevel returns specific level when preference is set`() = runTest {
        every { sharedPreferences.getString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.MODERATE.name) } returns ContentRestrictionLevel.STRICT.name

        service = AppPreferencesServiceImpl(sharedPreferences)
        val currentLevel = service.contentRestrictionLevel.first()

        assertEquals(ContentRestrictionLevel.STRICT, currentLevel)
    }

    @Test
    fun `setContentRestrictionLevel saves preference and updates state`() = runTest {
        every { editor.putString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.OFF.name) } returns editor

        service.setContentRestrictionLevel(ContentRestrictionLevel.OFF)

        verifyOrder {
            sharedPreferences.edit()
            editor.putString(CONTENT_RESTRICTION_KEY, ContentRestrictionLevel.OFF.name)
            editor.apply()
        }

        val updatedLevel = service.contentRestrictionLevel.first()
        assertEquals(ContentRestrictionLevel.OFF, updatedLevel)
    }
}