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
    fun `when initialized then calls initializeRatedMedia`() = runTest(mainDispatcher) {
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
    fun `when initializeRatedMedia succeeds then updates state with filtered data`() =
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
    fun `when initializeRatedMedia fails then updates error state`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        coEvery { manageRatingUseCase.getRatedMediaSorted() } throws Exception("Network error")
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)

        // When
        viewModel.initializeRatedMedia()
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.errorState).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when deleteMovieClick succeeds then updates state by filtering out deleted movie`() =
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
                val state = expectMostRecentItem()
                assertThat(state.isSnackBarVisible).isTrue()
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `when deleteTVShowClick succeeds then updates state by filtering out deleted tv show`() =
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
                val state = expectMostRecentItem()
                assertThat(state.isSnackBarVisible).isTrue()
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `when deleteMovieClick fails then updates error state`() = runTest(mainDispatcher) {
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
    fun `when deleteTVShowClick fails then updates error state`() = runTest(mainDispatcher) {
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
    fun `when rating category selected then updates selected category`() = runTest(mainDispatcher) {
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
    fun `when retry clicked then calls initializeRatedMedia`() = runTest(mainDispatcher) {
        // Given
        val manageRatingUseCase = mockk<ManageRatingUseCase>(relaxed = true)
        val mockRatedMedia = createMockRatedMedia()
        coEvery { manageRatingUseCase.getRatedMediaSorted() } returns mockRatedMedia
        val viewModel = MyRatingsViewModel(manageRatingUseCase = manageRatingUseCase)

        // When
        viewModel.onRetryClick()
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
    fun `when item clicked then emits movie navigation effect`() = runTest(mainDispatcher) {
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
    fun `when movie clicked then emits movie navigation effect`() = runTest(mainDispatcher) {
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
    fun `when tv show clicked then emits tv show navigation effect`() = runTest(mainDispatcher) {
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
    fun `when back clicked then emits back navigation effect`() = runTest(mainDispatcher) {
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
