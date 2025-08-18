package com.london.presentation.feature.account.account

import com.google.common.truth.Truth.assertThat
import com.london.domain.AppPreferencesService
import com.london.domain.entity.AccountInfo
import com.london.domain.entity.contentrestriction.ContentRestrictionLevel
import com.london.domain.entity.language.AppLanguage
import com.london.domain.entity.theme.AppTheme
import com.london.domain.usecase.accountdetails.GetAccountDetailsUseCase
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.presentation.feature.account.AccountViewModel
import com.london.presentation.feature.account.ActiveBottomSheet
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class   AccountViewModelTest {

    private lateinit var appPreferencesService: AppPreferencesService
    private lateinit var authenticationUseCase: AuthenticationUseCase
    private lateinit var accountDetailsUseCase: GetAccountDetailsUseCase
    private lateinit var viewModel: AccountViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        appPreferencesService = mockk(relaxed = true)
        authenticationUseCase = mockk(relaxed = true)
        accountDetailsUseCase = mockk(relaxed = true)
        
        Dispatchers.setMain(testDispatcher)
        
        every { appPreferencesService.isAppDarkMode } returns MutableStateFlow(false)
        every { appPreferencesService.appLanguage } returns MutableStateFlow(AppLanguage.ENGLISH)
        every { appPreferencesService.contentRestrictionLevel } returns MutableStateFlow(
            ContentRestrictionLevel.MODERATE)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should initialize with dark mode when preference is dark`() = runTest {
        // Given
        every { appPreferencesService.isAppDarkMode } returns MutableStateFlow(true)
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        coEvery { accountDetailsUseCase.invoke() } returns AccountInfo(1, "", "")

        // When
        viewModel =
            AccountViewModel(appPreferencesService, authenticationUseCase, accountDetailsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.appTheme).isEqualTo(AppTheme.DARK)
    }

    @Test
    fun `should initialize with Arabic language when preference is Arabic`() = runTest {
        // Given
        every { appPreferencesService.appLanguage } returns MutableStateFlow(AppLanguage.ARABIC)
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        coEvery { accountDetailsUseCase.invoke() } returns AccountInfo(1, "", "")

        // When
        viewModel =
            AccountViewModel(appPreferencesService, authenticationUseCase, accountDetailsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.appLanguage).isEqualTo(AppLanguage.ARABIC)
    }

    @Test
    fun `should handle logout click correctly`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.onLogoutClick()

        // Then
        advanceUntilIdle()
        assertThat(viewModel.state.value.showUserMenu).isFalse()
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.Logout)
    }

    @Test
    fun `should handle content restriction click`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.onContentRestrictionClick()

        // Then
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.ContentRestriction)
    }

    @Test
    fun `should save content restriction level`() = runTest {
        // Given
        setupViewModel()
        val newLevel = ContentRestrictionLevel.STRICT

        // When
        viewModel.onContentRestrictionSave(newLevel)

        // Then
        verify { appPreferencesService.setContentRestrictionLevel(newLevel) }
        assertThat(viewModel.state.value.currentContentRestriction).isEqualTo(newLevel)
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.None)
    }

    @Test
    fun `should handle appearance click`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.onAppearanceClick()

        // Then
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.Appearance)
    }

    @Test
    fun `should select dark mode`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.onDarkModeSelected()

        // Then
        assertThat(viewModel.state.value.appTheme).isEqualTo(AppTheme.DARK)
    }

    @Test
    fun `should select light mode`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.onLightModeSelected()

        // Then
        assertThat(viewModel.state.value.appTheme).isEqualTo(AppTheme.LIGHT)
    }

    @Test
    fun `should save appearance mode`() = runTest {
        // Given
        setupViewModel()
        viewModel.onDarkModeSelected()

        // When
        viewModel.onAppearanceModeSave()

        // Then
        verify { appPreferencesService.setAppTheme(AppTheme.DARK) }
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.None)
    }

    @Test
    fun `should show appearance bottom sheet`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.showAppearanceBottomSheet()

        // Then
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.Appearance)
    }

    @Test
    fun `should handle language click`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.onLanguageClick()

        // Then
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.Language)
    }

    @Test
    fun `should select English language`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.onEnglishSelected()

        // Then
        assertThat(viewModel.state.value.appLanguage).isEqualTo(AppLanguage.ENGLISH)
    }

    @Test
    fun `should select Arabic language`() = runTest {
        // Given
        setupViewModel()

        // When
        viewModel.onArabicSelected()

        // Then
        assertThat(viewModel.state.value.appLanguage).isEqualTo(AppLanguage.ARABIC)
    }

    @Test
    fun `should save language settings`() = runTest {
        // Given
        setupViewModel()
        viewModel.onArabicSelected()

        // When
        viewModel.onLanguageSettingsSave()

        // Then
        verify { appPreferencesService.setAppLanguage(AppLanguage.ARABIC) }
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.None)
    }

    @Test
    fun `should dismiss bottom sheet`() = runTest {
        // Given
        setupViewModel()
        viewModel.onAppearanceClick()
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.Appearance)
        assertThat(viewModel.state.value.showUserMenu).isFalse()

        // When
        viewModel.onBottomSheetDismiss()

        // Then
        assertThat(viewModel.state.value.activeBottomSheet).isEqualTo(ActiveBottomSheet.None)
        assertThat(viewModel.state.value.showUserMenu).isFalse()
    }

    @Test
    fun `should handle user not logged in during initialization`() = runTest {
        // Given
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        coEvery { accountDetailsUseCase.invoke() } returns AccountInfo(1, "", "")

        // When
        viewModel =
            AccountViewModel(appPreferencesService, authenticationUseCase, accountDetailsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isUserLoggedIn).isFalse()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `should update content restriction level when preference changes`() = runTest {
        // Given
        val contentRestrictionFlow = MutableStateFlow(ContentRestrictionLevel.MODERATE)
        every { appPreferencesService.contentRestrictionLevel } returns contentRestrictionFlow
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        coEvery { accountDetailsUseCase.invoke() } returns AccountInfo(1, "", "")

        // When
        viewModel =
            AccountViewModel(appPreferencesService, authenticationUseCase, accountDetailsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        
        contentRestrictionFlow.value = ContentRestrictionLevel.STRICT
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.currentContentRestriction).isEqualTo(
            ContentRestrictionLevel.STRICT)
    }

    private fun setupViewModel() {
        coEvery { authenticationUseCase.isLoggedIn() } returns true
        coEvery { accountDetailsUseCase.invoke() } returns AccountInfo(1, "TestUser", "avatar.jpg")

        viewModel =
            AccountViewModel(appPreferencesService, authenticationUseCase, accountDetailsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
    }
}