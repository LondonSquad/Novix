package com.london.presentation.feature.home.continuewatching

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContinueWatchingViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ContinueWatchingViewModel
    private val manageRecentMovieWatchedUseCase =
        mockk<ManageRecentMovieWatchedUseCase>(relaxed = true)
    private val manageRecentTvShowWatchedUseCase =
        mockk<ManageRecentTvShowWatchedUseCase>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery {
            manageRecentMovieWatchedUseCase.getAllWatchedMovies(any())
        } returns flowOf(emptyList())

        coEvery {
            manageRecentTvShowWatchedUseCase.getAllRecentTvShow(any())
        } returns flowOf(emptyList())

        viewModel = createViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createViewModel(): ContinueWatchingViewModel {
        return ContinueWatchingViewModel(
            manageRecentMovieWatchedUseCase = manageRecentMovieWatchedUseCase,
            manageRecentTvShowWatchedUseCase = manageRecentTvShowWatchedUseCase
        )
    }

    @Test
    fun `onFetchContinueWatchingList should return success when updates state correctly`() =
        runTest {

        // Given
        val mockMovies = listOf(createMockRecentMovie())
        val mockTvShows = listOf(createMockRecentTvShow())

        coEvery { manageRecentMovieWatchedUseCase.getMostRecent() } returns flowOf(mockMovies)
        coEvery { manageRecentTvShowWatchedUseCase.getMostRecent() } returns flowOf(mockTvShows)

        // When
        viewModel.getRecentWatchedMedia()
        advanceUntilIdle()
        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.error).isNull()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `onMovieGenreClick should not update selectedMovieGenre when same genre`() = runTest {

        // Given
        advanceUntilIdle()
        val initialGenre = viewModel.state.value.selectedMovieGenre

        // When
        viewModel.onMovieGenreClick(initialGenre)

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedMovieGenre).isEqualTo(initialGenre)
        }
    }

    @Test
    fun `onMovieGenreClick should update state and call getRecentWatchedMedia when different genre`() =
        runTest {
            // Given
            advanceUntilIdle()
            val initialGenre = viewModel.state.value.selectedMovieGenre
            val newGenre = MovieGenreUi.Action

            // When
            viewModel.onMovieGenreClick(newGenre)
            advanceUntilIdle()

            // Then
            val state = viewModel.state.value
            assertThat(state.selectedMovieGenre).isEqualTo(newGenre)
            assertThat(state.selectedMovieGenre).isNotEqualTo(initialGenre)
        }

    @Test
    fun `onTvShowGenreClick should not update selectedTvShowGenre when same genre`() = runTest {

        // Given
        advanceUntilIdle()
        val initialGenre = viewModel.state.value.selectedTvShowGenre

        // When
        viewModel.onTvShowGenreClick(initialGenre)

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedTvShowGenre).isEqualTo(initialGenre)
        }
    }

    @Test
    fun `onTvShowGenreClick should update state and call getRecentWatchedMedia when different genre`() =
        runTest {
            // Given
            advanceUntilIdle()
            val initialGenre = viewModel.state.value.selectedTvShowGenre
            val newGenre = TvShowGenreUi.Drama // Use a different genre

            // When
            viewModel.onTvShowGenreClick(newGenre)
            advanceUntilIdle()

            // Then
            val state = viewModel.state.value
            assertThat(state.selectedTvShowGenre).isEqualTo(newGenre)
            assertThat(state.selectedTvShowGenre).isNotEqualTo(initialGenre)
        }

    @Test
    fun `onMediaCategoryTabClick should not update selectedMediaCategory when same tab`() =
        runTest {

        // Given
        advanceUntilIdle()
        val initialTab = viewModel.state.value.selectedMediaCategory

        // When
        viewModel.onMediaCategoryTabClick(initialTab)

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedMediaCategory).isEqualTo(initialTab)
        }
    }

    @Test
    fun `onMediaCategoryTabClick should update selectedMediaCategory when different tab`() =
        runTest {
        // Given
        advanceUntilIdle()
        val initialTab = viewModel.state.value.selectedMediaCategory
        val newTab = com.london.presentation.shared.MediaCategory.TvShows

        // When
        viewModel.onMediaCategoryTabClick(newTab)

        // Then
        val state = viewModel.state.value
        assertThat(state.selectedMediaCategory).isEqualTo(newTab)
        assertThat(state.selectedMediaCategory).isNotEqualTo(initialTab)
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isInstanceOf(ContinueWatchingEffect.NavigateBack::class.java)
        }
    }

    @Test
    fun `onNavigateToMovieClick should emit when NavigateToMovieDetails effect`() = runTest {

        // Given
        val movieId = 1

        // When & Then
        viewModel.effect.test {
            viewModel.onNavigateToMovieClick(movieId)
            assertThat(awaitItem()).isInstanceOf(ContinueWatchingEffect.NavigateToMovieDetails::class.java)
        }
    }

    @Test
    fun `onNavigateToTvShowClick should emit when NavigateToTvShowDetails effect`() = runTest {

        // Given
        val tvShowId = 2
        // When & Then
        viewModel.effect.test {
            viewModel.onNavigateToTvShowClick(tvShowId)
            assertThat(awaitItem()).isInstanceOf(ContinueWatchingEffect.NavigateToTvShowDetails::class.java)
        }
    }

    private fun createMockRecentMovie() = mockk<Movie> {
        every { id } returns 1
        every { posterUrl } returns "/poster1.jpg"
        every { genres } returns listOf(MovieGenre.ALL)
        every { name } returns "Recent Movie"
        every { rating } returns 8
        every { releaseYear } returns 2023
    }

    private fun createMockRecentTvShow() = mockk<TvShow> {
        every { id } returns 2
        every { posterPicture } returns "/poster2.jpg"
        every { genres } returns listOf(TvShowGenre.ALL)
        every { name } returns "Recent TV Show"
        every { rating } returns 9
        every { releaseYear } returns 2022
    }
}