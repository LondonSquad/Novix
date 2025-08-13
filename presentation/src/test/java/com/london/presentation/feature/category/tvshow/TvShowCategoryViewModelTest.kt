package com.london.presentation.feature.category.tvshow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TvShowCategoryViewModelTest {
    private lateinit var manageTvShowDetailsUseCase: ManageTvShowDetailsUseCase
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private var viewModel: TvShowCategoryViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        manageTvShowDetailsUseCase = mockk(relaxed = true)
        every { savedStateHandle.getArgs<Screen.TvShowsByCategory>() } returns Screen.TvShowsByCategory(
            categoryId = CATEGORY_ID,
        )
        viewModel = TvShowCategoryViewModel(manageTvShowDetailsUseCase, savedStateHandle)
        coEvery {
            manageTvShowDetailsUseCase.getTvShowsByCategory(
                CATEGORY_ID,
                PAGE
            )
        } returns tvShowsPagingData
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `when initialization is called category id should be set`() = runTest {
        //When
        advanceUntilIdle()
        //Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.categoryId).isEqualTo(CATEGORY_ID)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initialization is called loading should be false after completion`() = runTest {
        // When
        advanceUntilIdle()
        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initialization is called tv shows flow should be created`() = runTest {
        // When
        advanceUntilIdle()
        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.tvShowFlow).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initialization should update state with error when use case throws`() = runTest {
        // Given
        coEvery {
            manageTvShowDetailsUseCase.getTvShowsByCategory(
                CATEGORY_ID,
                PAGE
            )
        } throws Exception()
        // When
        advanceUntilIdle()
        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.error).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onMovieClick should emit navigateToTvShowDetails effect`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onTvShowClick(tvShowId = 1)
            assertThat(awaitItem()).isInstanceOf(TvShowCategoryEffect.TvShowDetailsNavigation::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClick should emit NavigateBack effect`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBack()
            assertThat(awaitItem()).isInstanceOf(TvShowCategoryEffect.BackNavigation::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        const val CATEGORY_ID = 0
        const val PAGE = 1
        val tvShowsPagingData = PagedFetchResponse(
            items = listOf<TvShow>(),
            currentPage = PAGE,
            totalPages = 1,
            totalItems = 1
        )
    }
}
