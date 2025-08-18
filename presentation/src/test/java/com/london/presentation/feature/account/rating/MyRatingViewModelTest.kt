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
class MyRatingViewModelTest {
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
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)

        // When & Then
        viewModel.state.test {
            advanceUntilIdle()
            val state = expectMostRecentItem()
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
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)

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
            val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
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
            val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
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
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)

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
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)

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
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
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
    fun `onMovieClick should emit movie navigation effect when invoked`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
        val movieId = 1

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onMovieClick(movieId)
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.MovieDetailsNavigation(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTvShowClick should emit tv show navigation effect when invoked`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
        val tvShowId = 3

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onTvShowClick(tvShowId)
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.TvShowDetailsNavigation(tvShowId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBackClick should emit back navigation effect when invoked`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.BackNavigation)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onItemClick should emit movie navigation effect when movie is found`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
        val movieId = 1
        val mockMovie = RatedMedia(
            id = movieId,
            title = "Test Movie",
            posterPath = "/test.jpg",
            rating = 8,
            mediaType = MediaType.Movie
        )
        coEvery { manageRatingUseCase.getRatedMediaById(movieId) } returns mockMovie

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onItemClick(movieId)
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.MovieDetailsNavigation(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onItemClick should emit tv show navigation effect when tv show is found`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
        val tvShowId = 3
        val mockTvShow = RatedMedia(
            id = tvShowId,
            title = "Test TV Show",
            posterPath = "/test.jpg",
            rating = 9,
            mediaType = MediaType.TvShow
        )
        coEvery { manageRatingUseCase.getRatedMediaById(tvShowId) } returns mockTvShow

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onItemClick(tvShowId)
            assertThat(awaitItem()).isEqualTo(MyRatingEffect.TvShowDetailsNavigation(tvShowId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onItemClick should not emit effect when media is not found`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
        val nonExistentId = 999
        coEvery { manageRatingUseCase.getRatedMediaById(nonExistentId) } returns null

        advanceUntilIdle()

        // When & Then
        viewModel.effect.test {
            viewModel.onItemClick(nonExistentId)
            expectNoEvents()
        }
    }

    @Test
    fun `onItemClick should handle error when use case throws exception`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns createMockRatedMedia()
        val viewModel = MyRatingViewModel(manageRatingUseCase = manageRatingUseCase)
        val mediaId = 1
        coEvery { manageRatingUseCase.getRatedMediaById(mediaId) } throws RuntimeException("Network error")

        advanceUntilIdle()

        // When & Then
        viewModel.state.test {
            viewModel.onItemClick(mediaId)
            var state = awaitItem()
            while (state.errorState == null) {
                state = awaitItem()
            }
            assertThat(state.errorState).isNotNull()
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
