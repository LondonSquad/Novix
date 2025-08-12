package com.london.presentation.feature.register

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.presentation.feature.authentication.register.RegistrationEffect
import com.london.presentation.feature.authentication.register.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationViewModelTest {

    private lateinit var viewModel: RegistrationViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegistrationViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `onNavigateBack should emit NavigateBack effect`() = runTest(testDispatcher) {
        viewModel.effect.test {
            viewModel.onNavigateBack()
            assertThat(awaitItem()).isEqualTo(RegistrationEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPageLoaded with registration complete URL should emit RegistrationComplete effect`() = runTest(testDispatcher) {
        // When & Then
        viewModel.effect.test {
            viewModel.onPageLoaded(REGISTRATION_COMPLETION)
            assertThat(awaitItem()).isEqualTo(RegistrationEffect.RegistrationComplete)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPageLoaded with null URL should not emit any effect`() = runTest(testDispatcher) {
        viewModel.effect.test {
            viewModel.onPageLoaded(null)
            expectNoEvents()
        }
    }

    @Test
    fun `onPageLoaded with non-completion URL should not emit any effect`() = runTest(testDispatcher) {
        // When & Then
        viewModel.effect.test {
            viewModel.onPageLoaded(REGULAR_URL)
            expectNoEvents()
        }
    }

    @Test
    fun `when url changes `() = runTest (testDispatcher){
        viewModel.effect.test {
            viewModel.onUrlChanged(REGULAR_URL)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shouldInterceptUrl with cancel URL should emit NavigateBack effect`() = runTest(testDispatcher) {
        viewModel.effect.test {
            viewModel.shouldInterceptUrl(CANCEL)
            assertThat(awaitItem()).isEqualTo(RegistrationEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shouldInterceptUrl with registration complete URL should emit RegistrationComplete effect`() = runTest(testDispatcher) {
        viewModel.effect.test {
            viewModel.shouldInterceptUrl(REGISTRATION_COMPLETION)
            assertThat(awaitItem()).isEqualTo(RegistrationEffect.RegistrationComplete)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shouldInterceptUrl with allowed URL should return false`() = runTest(testDispatcher) {
        val result = viewModel.shouldInterceptUrl(SIGNUP)
        assertThat(result).isFalse()
    }

    @Test
    fun `shouldInterceptUrl with disallowed URL should return true`() = runTest(testDispatcher) {
        val result = viewModel.shouldInterceptUrl(MISS_MATCHED_URL)
        assertThat(result).isTrue()
    }

    @Test
    fun `shouldInterceptUrl with successful login URL should emit RegistrationComplete effect`() = runTest(testDispatcher) {
        viewModel.effect.test {
            viewModel.shouldInterceptUrl(LOGIN_SUCCESS)
            assertThat(awaitItem()).isEqualTo(RegistrationEffect.RegistrationComplete)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shouldInterceptUrl with back URL should emit NavigateBack effect`() = runTest(testDispatcher) {
        viewModel.effect.test {
            viewModel.shouldInterceptUrl(NAVIGATE_BACK)
            assertThat(awaitItem()).isEqualTo(RegistrationEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object Registration {
        const val REGULAR_URL = "https://themoviedb.org/signup"
        const val REGISTRATION_COMPLETION = "https://themoviedb.org/account/verify"
        const val NAVIGATE_BACK = "https://themoviedb.org/back"
        const val LOGIN_SUCCESS = "https://themoviedb.org/login/success"
        const val SIGNUP = "https://themoviedb.org/signup"
        const val CANCEL = "https://themoviedb.org/cancel"
        const val MISS_MATCHED_URL = "https://example.com"
    }
}