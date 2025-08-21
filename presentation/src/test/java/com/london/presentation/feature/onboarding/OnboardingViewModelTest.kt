package com.london.presentation.feature.onboarding

import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.service.AppPreferencesService
import com.london.presentation.feature.welcome.onboarding.OnboardingEffect
import com.london.presentation.feature.welcome.onboarding.OnboardingViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {
    private lateinit var appPreferencesService: AppPreferencesService
    private val pagerState = mockk<PagerState>(relaxed = true)
    private var viewModel: OnboardingViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        MockKAnnotations.init(this)
        setupDefaultMocks()
        viewModel = createViewModel()
    }

    private fun setupDefaultMocks() {
        appPreferencesService = mockk()
        every { pagerState.currentPage } returns 0
        every { pagerState.pageCount } returns 3
        coEvery { pagerState.animateScrollToPage(any(), any()) } just Runs
        coEvery { appPreferencesService.setOnBoardingShown() } just Runs
    }

    private fun createViewModel() = OnboardingViewModel(appPreferencesService)

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `when viewModel is initialized, currentPage should be 0`(): Unit = runTest {
        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.currentPage).isEqualTo(0)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when page is changed, currentPage should be updated`() = runTest {
        // Given
        val targetPage = 2

        // When
        viewModel?.onPageChanged(targetPage)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.currentPage).isEqualTo(targetPage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when scrollToPage is called with valid page, animation should be triggered`() = runTest {
        // Given
        val targetPage = 1
        val scope = this

        // When
        viewModel?.scrollToPage(pagerState, targetPage, scope)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            pagerState.animateScrollToPage(
                page = targetPage,
                animationSpec = any()
            )
        }
    }

    @Test
    fun `when scrollToPage is called with invalid negative page, animation should not be triggered`() =
        runTest {
            // Given
            val invalidPage = -1
            val scope = this

            // When
            viewModel?.scrollToPage(pagerState, invalidPage, scope)
            advanceUntilIdle()

            // Then
            coVerify(exactly = 0) {
                pagerState.animateScrollToPage(any(), any())
            }
        }

    @Test
    fun `when scrollToPage is called with page exceeding count, animation should not be triggered`() =
        runTest {
            // Given
            val invalidPage = 5 // pageCount is 3
            val scope = this

            // When
            viewModel?.scrollToPage(pagerState, invalidPage, scope)
            advanceUntilIdle()

            // Then
            coVerify(exactly = 0) {
                pagerState.animateScrollToPage(any(), any())
            }
        }

    @Test
    fun `when scrollPrevious is called from middle page, previous page should be navigated`() =
        runTest {
            // Given
            every { pagerState.currentPage } returns 2
            val scope = this

            // When
            viewModel?.scrollPrevious(pagerState, scope)
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) {
                pagerState.animateScrollToPage(
                    page = 1,
                    animationSpec = any()
                )
            }
        }

    @Test
    fun `when scrollPrevious is called from first page, navigation should not happen`() = runTest {
        // Given
        every { pagerState.currentPage } returns 0
        val scope = this

        // When
        viewModel?.scrollPrevious(pagerState, scope)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) {
            pagerState.animateScrollToPage(any(), any())
        }
    }

    @Test
    fun `when scrollNext is called from middle page, next page should be navigated`() = runTest {
        // Given
        every { pagerState.currentPage } returns 1
        val scope = this

        // When
        viewModel?.scrollNext(pagerState, scope)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            pagerState.animateScrollToPage(
                page = 2,
                animationSpec = any()
            )
        }
    }

    @Test
    fun `when scrollNext is called from last page, welcome navigation effect should be emitted`() =
        runTest {
            // Given
            every { pagerState.currentPage } returns 2 // Last page
            val scope = this

            // When & Then
            viewModel?.effect?.test {
                viewModel?.scrollNext(pagerState, scope)
                advanceUntilIdle()
                assertThat(awaitItem()).isEqualTo(OnboardingEffect.OnWelcomeNavigation)
            }
        }

    @Test
    fun `when scrollNext is called from last page, onboarding should be marked as shown`() =
        runTest {
            // Given
            every { pagerState.currentPage } returns 2 // Last page
            val scope = this

            // When
            viewModel?.scrollNext(pagerState, scope)
            advanceUntilIdle()

            // Then - Use timeout for IO operations
            coVerify(timeout = 2000, exactly = 1) {
                appPreferencesService.setOnBoardingShown()
            }
        }

    @Test
    fun `when navigateToWelcome is called, welcome navigation effect should be emitted`() =
        runTest {
            // When & Then
            viewModel?.effect?.test {
                viewModel?.navigateToWelcome()

                withTimeout(2000) {
                    assertThat(awaitItem()).isEqualTo(OnboardingEffect.OnWelcomeNavigation)
                }
            }
        }

    @Test
    fun `when onboardingFinished is called, preferences service should be invoked`() = runTest {
        // When
        viewModel?.onboardingFinished()
        advanceUntilIdle()

        coVerify(timeout = 2000, exactly = 1) {
            appPreferencesService.setOnBoardingShown()
        }
    }

    @Test
    fun `when onboardingFinished fails, exception should be handled gracefully`() = runTest {
        // Given
        coEvery { appPreferencesService.setOnBoardingShown() } throws Exception("Network error")

        // When
        viewModel?.onboardingFinished()
        advanceUntilIdle()

        coVerify(timeout = 2000, exactly = 1) {
            appPreferencesService.setOnBoardingShown()
        }
    }

    @Test
    fun `when navigateToWelcome is called, onboarding should be marked as shown`() = runTest {
        // When
        viewModel?.navigateToWelcome()
        advanceUntilIdle()

        coVerify(timeout = 2000, exactly = 1) {
            appPreferencesService.setOnBoardingShown()
        }
    }

    @Test
    fun `when page changes multiple times, state should be updated correctly`() = runTest {
        // When
        viewModel?.onPageChanged(1)
        advanceUntilIdle()

        viewModel?.state?.test {
            val firstState = expectMostRecentItem()
            assertThat(firstState.currentPage).isEqualTo(1)

            viewModel?.onPageChanged(2)
            advanceUntilIdle()

            val secondState = expectMostRecentItem()
            assertThat(secondState.currentPage).isEqualTo(2)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when scrollToPage is called with first page, animation should work correctly`() = runTest {
        // Given
        val firstPage = 0
        val scope = this

        // When
        viewModel?.scrollToPage(pagerState, firstPage, scope)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            pagerState.animateScrollToPage(
                page = firstPage,
                animationSpec = any()
            )
        }
    }

    @Test
    fun `when scrollToPage is called with last page, animation should work correctly`() = runTest {
        // Given
        val lastPage = 2 // pageCount - 1
        val scope = this

        // When
        viewModel?.scrollToPage(pagerState, lastPage, scope)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            pagerState.animateScrollToPage(
                page = lastPage,
                animationSpec = any()
            )
        }
    }
}
