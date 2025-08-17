package com.london.presentation.feature.category.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.toDomain
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
class MovieCategoryViewModelTest {
    private lateinit var getMovieUseCase: GetMovieUseCase
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private var viewModel: MovieCategoryViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        getMovieUseCase = mockk(relaxed = true)
        every { savedStateHandle.getArgs<Screen.MoviesByCategory>() } returns Screen.MoviesByCategory(
            category = CATEGORY,
        )
        viewModel = MovieCategoryViewModel(getMovieUseCase, savedStateHandle)
        coEvery { getMovieUseCase.getMoviesByGenre(CATEGORY.toDomain(), PAGE) } returns moviesPagingData
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
            assertThat(state.genre).isEqualTo(CATEGORY)
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
    fun `when initialization is called movies flow should be created`() = runTest {
        // When
        advanceUntilIdle()
        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initialization should update state with error when use case throws`() = runTest {
        // Given
        coEvery { getMovieUseCase.getMoviesByGenre(CATEGORY.toDomain(), PAGE) } throws Exception()
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
    fun `onMovieClick should emit navigateToMovieDetails effect`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onMovieClick(movieId = 1)
            assertThat(awaitItem()).isInstanceOf(MovieCategoryEffect.MovieDetailsNavigation::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClick should emit NavigateBack effect`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBackClick()
            assertThat(awaitItem()).isInstanceOf(MovieCategoryEffect.BackNavigation::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        val CATEGORY = MovieGenreUi.All
        const val PAGE = 1
        val moviesPagingData = PagedFetchResponse(
            items = listOf<Movie>(),
            currentPage = PAGE,
            totalPages = 1,
            totalItems = 1
        )
    }
}
