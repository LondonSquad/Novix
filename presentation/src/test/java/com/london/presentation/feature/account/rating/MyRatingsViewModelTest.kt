package com.london.presentation.feature.account.rating

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.usecase.rating.ManageRatingUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyRatingsViewModelTest {
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `initializeRatedMedia should load movies and tv shows when viewModel is initialized`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)

        // When
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.ratedMovies).isNotEmpty()
            assertThat(state.ratedTvShows).isNotEmpty()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `initializeRatedMedia should update state with filtered data when use case succeeds`() =
        runTest(mainDispatcher) {
            // Given
            val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
            val mockRatedMedia = createMockRatedMedia()
            coEvery { manageRatingUseCase.getRatedMediaSorted() } returns mockRatedMedia
            val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)

            // When
            viewModel.initializeRatedMedia()
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val state = expectMostRecentItem()
                assertThat(state.isLoading).isFalse()
                assertThat(state.ratedMovies).isNotEmpty()
                assertThat(state.ratedTvShows).isNotEmpty()
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `initializeRatedMedia should update error state when use case fails`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } throws Exception("Network error")
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)

        // When
        viewModel.initializeRatedMedia()
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            var state = awaitItem()
            while (state.errorState == null) {
                state = awaitItem()
            }
            assertThat(state.errorState).isNotNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDeleteMovieClick should show snackBar when deletion succeeds`() =
        runTest(mainDispatcher) {
            // Given
            val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
            coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
            val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)
            val movieId = 1
            coEvery { manageRatingUseCase.deleteMovieRating(movieId) } returns true

            advanceUntilIdle()

            // When
            viewModel.onDeleteMovieClick(movieId)
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                var state = awaitItem()
                while (!state.isSnackBarVisible) {
                    state = awaitItem()
                }
                assertThat(state.isSnackBarVisible).isTrue()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onDeleteTVShowClick should show snackBar when deletion succeeds`() =
        runTest(mainDispatcher) {
            // Given
            val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
            coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
            val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)
            val tvShowId = 3
            coEvery { manageRatingUseCase.deleteTvShowRating(tvShowId) } returns true

            advanceUntilIdle()

            // When
            viewModel.onDeleteTVShowClick(tvShowId)
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                var state = awaitItem()
                while (!state.isSnackBarVisible) {
                    state = awaitItem()
                }
                assertThat(state.isSnackBarVisible).isTrue()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onDeleteMovieClick should update error state when deletion fails`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        coEvery { manageRatingUseCase.deleteMovieRating(1) } throws RuntimeException("Delete failed")
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)

        advanceUntilIdle()

        // When & Then
        viewModel.state.test {
            viewModel.onDeleteMovieClick(1)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDeleteTVShowClick should update error state when deletion fails`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        coEvery { manageRatingUseCase.deleteTvShowRating(3) } throws RuntimeException("Delete failed")
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)

        advanceUntilIdle()

        // When & Then
        viewModel.state.test {
            viewModel.onDeleteTVShowClick(3)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRatingCategorySelected should update selected category when invoked`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)
        val category = RatingCategory.Movies

        // When
        viewModel.onRatingCategorySelected(category)

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedRatingCategory).isEqualTo(category)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onItemClick should emit movie navigation effect when invoked`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)
        val movieId = 1

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onItemClick(movieId)
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.ToMovieNavigation(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClick should emit movie navigation effect when invoked`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)
        val movieId = 1

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onMovieClick(movieId)
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.ToMovieNavigation(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTvShowClick should emit tv show navigation effect when invoked`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)
        val tvShowId = 3

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onTvShowClick(tvShowId)
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.ToTvShowNavigation(tvShowId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBackClick should emit back navigation effect when invoked`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.BackNavigation)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createMockRatedMedia(): List<RatedMedia> {
        return listOf(
            RatedMedia(
                id = 1,
                title = "Test Movie 1",
                posterPath = "/test1.jpg",
                rating = 8,
                mediaType = MediaType.Movie
            ),
            RatedMedia(
                id = 2,
                title = "Test Movie 2",
                posterPath = "/test2.jpg",
                rating = 7,
                mediaType = MediaType.Movie
            ),
            RatedMedia(
                id = 3,
                title = "Test TV Show 1",
                posterPath = "/test3.jpg",
                rating = 9,
                mediaType = MediaType.TvShow
            )
        )
    }
}
