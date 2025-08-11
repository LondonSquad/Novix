package com.london.presentation.feature.details.tvshow.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.movie.ManageMovieDetailsUseCase
import com.london.domain.usecase.rating.RatingUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.presentation.feature.details.movie.MovieDetailsEffect
import com.london.presentation.feature.details.movie.MovieDetailsViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
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

    @MockK
    private lateinit var movieDetails: ManageMovieDetailsUseCase
    @MockK
    private lateinit var manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase
    @MockK
    private lateinit var manageRecentViewedUseCase: ManageRecentViewedUseCase
    @MockK
    private lateinit var ratingUseCase: RatingUseCase
    @MockK
    private lateinit var authenticationUseCase: AuthenticationUseCase
    @MockK(relaxed = true)
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: MovieDetailsViewModel
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        MockKAnnotations.init(this)
        movieDetails = mockk()
        manageRecentMovieWatchedUseCase = mockk()
        manageRecentViewedUseCase = mockk()
        ratingUseCase = mockk()
        authenticationUseCase = mockk()
        setupDefaultMocks()
        viewModel = createViewModel()
    }

    private fun setupDefaultMocks() {
        every { savedStateHandle.getArgs<Screen.MovieDetails>() } returns Screen.MovieDetails(
            MOVIE_ID
        )
        coEvery { movieDetails.getMovieDetails(MOVIE_ID) } returns mockMovieDetails
        coEvery { movieDetails.getMovieImages(MOVIE_ID) } returns mockMovieImages
        coEvery { movieDetails.getMovieCast(MOVIE_ID) } returns mockMovieCast
        coEvery { movieDetails.getSimilarMovies(MOVIE_ID) } returns mockSimilarMovies
        coEvery { movieDetails.getMovieVideo(MOVIE_ID) } returns mockMovieVideos
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        coEvery { ratingUseCase.getRateAccountMovieStatesById(MOVIE_ID) } returns 0
        coEvery { manageRecentMovieWatchedUseCase.addMovieToRecentWatched(any()) } returns Unit
        coEvery { manageRecentViewedUseCase.addToRecentViewed(any()) } returns Unit
    }

    private fun createViewModel() = MovieDetailsViewModel(
        movieDetails,
        manageRecentMovieWatchedUseCase,
        manageRecentViewedUseCase,
        ratingUseCase,
        authenticationUseCase,
        savedStateHandle,
        movieIdOverride = MOVIE_ID
    )

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `when loadMovieDetails succeeds, all movie details should be populated correctly`() =
        runTest {
            advanceUntilIdle()

            viewModel.state.test {
                val state = awaitItem()

                assertThat(state.isLoading).isFalse()
                assertThat(state.error).isNull()
                assertThat(state.movieId).isEqualTo(mockMovieDetails.id)
                assertThat(state.movieName).isEqualTo(mockMovieDetails.title)
                assertThat(state.movieRating).isEqualTo(mockMovieDetails.voteAverage)
            }
        }

    @Test
    fun `when loadSimilarAndVideos succeeds, should populate similar movies and video`() = runTest {
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.similarMovies).isEqualTo(mockSimilarMovies)
            assertThat(state.movieVideo).isEqualTo(mockMovieVideos.first())
        }
    }

    @Test
    fun `when loadSimilarAndVideos fails, should show error state`() = runTest {
        coEvery { movieDetails.getSimilarMovies(MOVIE_ID) } throws Exception("Network error")

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNotNull()
        }
    }

    @Test
    fun `when primary initialization fails, should show error state`() = runTest {
        coEvery { movieDetails.getMovieDetails(MOVIE_ID) } throws Exception("Network error")

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNotNull()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `when user is authenticated and has rated movie, should show as rated`() = runTest {
        coEvery { authenticationUseCase.isLoggedIn() } returns true
        coEvery { ratingUseCase.getRateAccountMovieStatesById(MOVIE_ID) } returns 5

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isRated).isTrue()
        }
    }

    @Test
    fun `when getMovieVideo returns empty list, should set empty video and fallback images`() =
        runTest {
            coEvery { movieDetails.getMovieVideo(MOVIE_ID) } returns emptyList()
            coEvery { movieDetails.getMovieImages(MOVIE_ID) } returns emptyList()
            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.state.test {
                val state = awaitItem()
                assertThat(state.movieVideo).isEmpty()
                assertThat(state.movieImages).containsExactly(mockMovieDetails.posterUrl)
            }
        }

    @Test
    fun `when rating succeeds, should update rating state and hide bottom sheet`() = runTest {
        val testRating = 8
        coEvery { ratingUseCase.addMovieRatingById(MOVIE_ID, testRating) } returns true
        coEvery { ratingUseCase.getRateAccountMovieStatesById(MOVIE_ID) } returns testRating

        viewModel.onSelectRatingClick(testRating)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.selectedRating).isEqualTo(testRating)
            assertThat(state.isRated).isTrue()
            assertThat(state.isRateBottomSheetVisible).isFalse()
            assertThat(state.isSuccessfullyRated).isTrue()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `when user is not authenticated or has no rating, should show as not rated`() = runTest {
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isRated).isFalse()
        }
    }

    @Test
    fun `when authenticated user clicks rate button, should show rate bottom sheet - Solution 2`() =
        runTest {
        coEvery { authenticationUseCase.isLoggedIn() } returns true
        viewModel = createViewModel()

            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.isRateBottomSheetVisible).isFalse() // Verify initial state

                viewModel.onRateBottomSheetClick()

                val updatedState = awaitItem()
                assertThat(updatedState.isRateBottomSheetVisible).isTrue()
        }
    }

    @Test
    fun `when guest user clicks rate button, should show guest user bottom sheet`() = runTest {
        coEvery { authenticationUseCase.isLoggedIn() } returns false
        viewModel = createViewModel()

        viewModel.onRateBottomSheetClick()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isGuestUserBottomSheetVisible).isTrue()
            assertThat(state.isGuestUser).isTrue()
        }
    }

    @Test
    fun `should emit correct navigation effects for user actions`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.BackNavigation)

            viewModel.onMovieClick(123)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.MovieNavigation(123))

            viewModel.onActorClick(456)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.ActorNavigation(456))

            viewModel.onLoginClick()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.OnLoginNavigation)

            viewModel.onReviewsClick(789, 1)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.ReviewsNavigation(789, 1))

            viewModel.onGenreClick(28)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.GenreNavigation(28))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onExpandClick should toggle expanded state correctly`() = runTest {
        viewModel.onExpandClick()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.expanded).isTrue()

        }

        viewModel.onExpandClick()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.expanded).isFalse()
        }
    }

    @Test
    fun `onRetry should clear error and reload data`() = runTest {
        coEvery { movieDetails.getMovieDetails(MOVIE_ID) } throws Exception("Network error")
        advanceUntilIdle()

        coEvery { movieDetails.getMovieDetails(MOVIE_ID) } returns mockMovieDetails

        viewModel.onRetry()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNull()
            assertThat(state.movieId).isEqualTo(mockMovieDetails.id)
        }
    }

    @Test
    fun `should add movie to recent watched and viewed during initialization`() = runTest {
        advanceUntilIdle()

        coVerify { manageRecentMovieWatchedUseCase.addMovieToRecentWatched(any()) }
        coVerify { manageRecentViewedUseCase.addToRecentViewed(any()) }
    }

    @Test
    fun `when movieId is null in savedStateHandle, should use default value 0`() = runTest {
        every { savedStateHandle.getArgs<Screen.MovieDetails>() } returns null
        val testViewModel = MovieDetailsViewModel(
            movieDetails = movieDetails,
            manageRecentMovieWatchedUseCase = manageRecentMovieWatchedUseCase,
            manageRecentViewedUseCase = manageRecentViewedUseCase,
            ratingUseCase = ratingUseCase,
            authenticationUseCase = authenticationUseCase,
            savedStateHandle = savedStateHandle
        )
        advanceUntilIdle()

        coVerify { movieDetails.getMovieDetails(0) }
        testViewModel.viewModelScope.cancel()
    }

    @Test
    fun `when rating fails, should show error and reset success state`() = runTest {
        val testRating = 8
        coEvery {
            ratingUseCase.addMovieRatingById(MOVIE_ID, testRating)
        } throws Exception("Rating failed")

        viewModel.onSelectRatingClick(testRating)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isSuccessfullyRated).isNull()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `when getMovieImages returns empty list, should use poster as fallback`() = runTest {
        coEvery { movieDetails.getMovieImages(MOVIE_ID) } returns emptyList()
        viewModel = createViewModel()
        advanceUntilIdle()

        //this delay is needed cause it depend on IO dispatcher
        delay(50)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.movieImages).containsExactly(mockMovieDetails.posterUrl)
        }
    }

    companion object {
        private const val MOVIE_ID = 12345

        private val mockMovieDetails = mockk<MovieDetails>(relaxed = true) {
            every { id } returns MOVIE_ID
            every { title } returns "Test Movie"
            every { genresId } returns listOf(1, 2, 3)
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
