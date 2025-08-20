package com.london.presentation.feature.details.movie.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.actor.Actor
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.movie.MovieDetails
import com.london.domain.entity.shared.MediaType
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.presentation.feature.details.movie.MovieDetailsEffect
import com.london.presentation.feature.details.movie.MovieDetailsViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.genre.MovieGenreUi
import io.mockk.MockKAnnotations
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
class MovieDetailsViewModelTest {
    private lateinit var getMovieUseCase: GetMovieUseCase
    private lateinit var manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase
    private lateinit var manageRecentViewedUseCase: ManageRecentViewedUseCase
    private lateinit var ratingUseCase: ManageRatingUseCase
    private lateinit var authenticationUseCase: AuthenticationUseCase
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private var viewModel: MovieDetailsViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        MockKAnnotations.init(this)
        setupDefaultMocks()
        viewModel = createViewModel()
    }

    private fun setupDefaultMocks() {
        authenticationUseCase = mockk()
        ratingUseCase = mockk()
        manageRecentViewedUseCase = mockk()
        manageRecentMovieWatchedUseCase = mockk()
        getMovieUseCase = mockk()
        every { savedStateHandle.getArgs<Screen.MovieDetails>() } returns Screen.MovieDetails(
            MOVIE_ID
        )
        coEvery { getMovieUseCase.getMovieDetails(any()) } returns mockMovieDetails
        coEvery { getMovieUseCase.getMovieImages(any()) } returns mockMovieImages
        coEvery { getMovieUseCase.getMovieCast(any()) } returns mockMovieCast
        coEvery { getMovieUseCase.getSimilarMovies(any()) } returns mockSimilarMovies
        coEvery { getMovieUseCase.getMovieVideo(any()) } returns mockMovieVideos
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        coEvery { ratingUseCase.getRateAccountMovieStatesById(any()) } returns 0
        coEvery { manageRecentMovieWatchedUseCase.addMovieToRecentWatched(any()) } returns Unit
        coEvery { manageRecentViewedUseCase.addToRecentViewed(any()) } returns Unit

        viewModel = createViewModel()
    }

    private fun createViewModel() = MovieDetailsViewModel(
        savedStateHandle,
        getMovieUseCase,
        ratingUseCase,
        authenticationUseCase,
        manageRecentViewedUseCase,
        manageRecentMovieWatchedUseCase,
    )

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `when main movie data loads successfully, movie details should be populated`() = runTest {

        // When & Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            advanceUntilIdle()
            assertThat(state.movieId).isEqualTo(mockMovieDetails.id)
            assertThat(state.movieName).isEqualTo(mockMovieDetails.title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when user is not authenticated, isRated should be false`() = runTest {

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.isRated).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when authenticated user clicks rate button, rate bottom sheet should be shown`() =
        runTest {

            // Given
            coEvery { authenticationUseCase.isLoggedIn() } returns true

            // Then
            advanceUntilIdle()

            // Then
            viewModel?.state?.test {
                val initialState = awaitItem()
                assertThat(initialState.isRateBottomSheetVisible).isFalse()

                viewModel?.onRateBottomSheetClick()
                advanceUntilIdle()

                val updatedState = awaitItem()
                assertThat(updatedState.isRateBottomSheetVisible).isTrue()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when back button is clicked, back navigation effect should be emitted`() = runTest {

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBackClick()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.BackNavigation)
        }
    }

    @Test
    fun `when movie is clicked, movie navigation effect should be emitted`() = runTest {

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onMovieClick(123)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.MovieNavigation(123))
        }
    }

    @Test
    fun `when actor is clicked, actor navigation effect should be emitted`() = runTest {

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onActorClick(456)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.ActorNavigation(456))
        }
    }

    @Test
    fun `when login is clicked, login navigation effect should be emitted`() = runTest {

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onLoginClick()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.LoginNavigation)
        }
    }

    @Test
    fun `when reviews is clicked, reviews navigation effect should be emitted`() = runTest {

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onReviewsClick(789, MediaType.Movie)
            assertThat(awaitItem()).isEqualTo(
                MovieDetailsEffect.ReviewsNavigation(
                    789,
                    MediaType.Movie
                )
            )
        }
    }

    @Test
    fun `when genre is clicked, genre navigation effect should be emitted`() = runTest {

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onGenreClick(MovieGenreUi.Action)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.GenreNavigation(MovieGenreUi.Action))
        }
    }

    @Test
    fun `when expand is clicked first time, expanded state should be true`() = runTest {

        // When
        viewModel?.onExpandClick()
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.expanded).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when expand is clicked twice, expanded state should be false`() = runTest {

        // When
        viewModel?.onExpandClick()
        viewModel?.onExpandClick()
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.expanded).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when retry is clicked after error, error should be cleared`() = runTest {

        // Given
        coEvery { getMovieUseCase.getMovieDetails(any()) } throws Exception("Network error")
        advanceUntilIdle()

        coEvery { getMovieUseCase.getMovieDetails(any()) } returns mockMovieDetails

        // When
        viewModel?.onRetryClick()
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.error).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when retry is clicked after error, data should be reloaded`() = runTest {

        // Given
        coEvery { getMovieUseCase.getMovieDetails(any()) } throws Exception("Network error")
        advanceUntilIdle()

        coEvery { getMovieUseCase.getMovieDetails(any()) } returns mockMovieDetails

        // When
        viewModel?.onRetryClick()
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.movieId).isEqualTo(mockMovieDetails.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private const val MOVIE_ID = 12345

        private val mockMovieDetails = mockk<MovieDetails>(relaxed = true) {
            every { id } returns MOVIE_ID
            every { title } returns "Test Movie"
            every { voteAverage } returns "8.5"
            every { runtime } returns 120
            every { releaseDate } returns "2023-01-01"
            every { overview } returns "Test overview"
            every { posterUrl } returns "test_poster_url"
        }

        private val mockMovieImages = listOf("image1.jpg", "image2.jpg")

        private val mockMovieCast = listOf<Actor>(
            mockk(relaxed = true),
            mockk(relaxed = true)
        )

        private val mockSimilarMovies = listOf<Movie>(
            mockk(relaxed = true),
            mockk(relaxed = true)
        )

        private val mockMovieVideos = listOf("video1_url", "video2_url")
    }
}
