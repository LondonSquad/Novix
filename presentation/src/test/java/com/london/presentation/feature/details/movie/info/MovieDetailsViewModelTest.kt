package com.london.presentation.feature.details.movie.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.rating.ManageRatingUseCase
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
    }

    private fun createViewModel() = MovieDetailsViewModel(
        getMovieUseCase,
        manageRecentMovieWatchedUseCase,
        manageRecentViewedUseCase,
        ratingUseCase,
        authenticationUseCase,
        savedStateHandle
    )

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `when loadMovieDetails succeeds, all movie details should be populated correctly`() =
        runTest {
            advanceUntilIdle()

            viewModel?.state?.test {
                val state = awaitItem()
                assertThat(state.isLoading).isFalse()
                assertThat(state.error).isNull()
                assertThat(state.movieId).isEqualTo(mockMovieDetails.id)
                assertThat(state.movieName).isEqualTo(mockMovieDetails.title)
                assertThat(state.movieRating).isEqualTo(mockMovieDetails.voteAverage)
            }
        }

    @Test
    fun `loadSimilarAndVideos should populate similar movies and video when succeeds`() = runTest {
        advanceUntilIdle()

        viewModel?.state?.test {
            val state = awaitItem()
            assertThat(state.similarMovies).isEqualTo(mockSimilarMovies)
            assertThat(state.movieVideo).isEqualTo(mockMovieVideos.first())
        }
    }

    @Test
    fun `loadSimilarAndVideos should show error state when fails`() = runTest {
        coEvery { getMovieUseCase.getSimilarMovies(any()) } throws Exception("Network error")

        val testViewModel = createViewModel()
        advanceUntilIdle()

        testViewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNotNull()
        }
        testViewModel.viewModelScope.cancel()
    }

    @Test
    fun `should show error state, when primary initialization fails`() = runTest {
        coEvery { getMovieUseCase.getMovieDetails(any()) } throws Exception("Network error")

        val testViewModel = createViewModel()
        advanceUntilIdle()

        testViewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNotNull()
            assertThat(state.isLoading).isFalse()
        }
        testViewModel.viewModelScope.cancel()
    }

    @Test
    fun `getRateAccountMovieStates should show rated when user is authenticated and has rated movie`() =
        runTest {
            coEvery { authenticationUseCase.isLoggedIn() } returns true
            coEvery { ratingUseCase.getRateAccountMovieStatesById(any()) } returns 5

            val testViewModel = createViewModel()
            advanceUntilIdle()

            testViewModel.state.test {
                val state = awaitItem()
                assertThat(state.isRated).isTrue()
            }
            testViewModel.viewModelScope.cancel()
        }


    @Test
    fun `onSelectRatingClick should update rating state and hide bottom sheet when rating succeeds`() =
        runTest {
            val testRating = 8
            coEvery { ratingUseCase.addMovieRatingById(any(), testRating) } returns true
            coEvery { ratingUseCase.getRateAccountMovieStatesById(any()) } returns testRating

            viewModel?.onSelectRatingClick(testRating)
            advanceUntilIdle()

            viewModel?.state?.test {
                val state = awaitItem()
                assertThat(state.selectedRating).isEqualTo(testRating)
                assertThat(state.isRated).isTrue()
                assertThat(state.isRateBottomSheetVisible).isFalse()
                assertThat(state.isSuccessfullyRated).isTrue()
                assertThat(state.isLoading).isFalse()
            }
        }

    @Test
    fun `getRateAccountMovieStates should show not rated when user is not authenticated or has no rating`() =
        runTest {
            advanceUntilIdle()

            viewModel?.state?.test {
                val state = awaitItem()
                assertThat(state.isRated).isFalse()
            }
        }

    @Test
    fun `onRateBottomSheetClick should show rate bottom sheet when authenticated user clicks rate button`() =
        runTest {
            coEvery { authenticationUseCase.isLoggedIn() } returns true
            val testViewModel = createViewModel()
            advanceUntilIdle()

            testViewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.isRateBottomSheetVisible).isFalse()

                testViewModel.onRateBottomSheetClick()

                val updatedState = awaitItem()
                assertThat(updatedState.isRateBottomSheetVisible).isTrue()
            }
            testViewModel.viewModelScope.cancel()
        }

    @Test
    fun `onRateBottomSheetClick should show guest user bottom sheet when guest user clicks rate button`() =
        runTest {
            coEvery { authenticationUseCase.isLoggedIn() } returns false
            val testViewModel = createViewModel()

            testViewModel.onRateBottomSheetClick()
            advanceUntilIdle()

            testViewModel.state.test {
                val state = awaitItem()
                assertThat(state.isGuestUserBottomSheetVisible).isTrue()
                assertThat(state.isGuestUser).isTrue()
            }
            testViewModel.viewModelScope.cancel()
        }

    @Test
    fun `onUserActions should emit correct navigation effects`() = runTest {
        viewModel?.effect?.test {
            viewModel?.onBackClick()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.BackNavigation)

            viewModel?.onMovieClick(123)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.MovieNavigation(123))

            viewModel?.onActorClick(456)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.ActorNavigation(456))

            viewModel?.onLoginClick()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.OnLoginNavigation)

            viewModel?.onReviewsClick(789, 1)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.ReviewsNavigation(789, 1))

            viewModel?.onGenreClick(28)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.GenreNavigation(28))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onExpandClick should toggle expanded state correctly`() = runTest {
        viewModel?.onExpandClick()
        advanceUntilIdle()

        viewModel?.state?.test {
            val state = awaitItem()
            assertThat(state.expanded).isTrue()
        }

        viewModel?.onExpandClick()
        advanceUntilIdle()

        viewModel?.state?.test {
            val state = awaitItem()
            assertThat(state.expanded).isFalse()
        }
    }

    @Test
    fun `onRetry should clear error and reload data`() = runTest {
        coEvery { getMovieUseCase.getMovieDetails(any()) } throws Exception("Network error")
        val testViewModel = createViewModel()
        advanceUntilIdle()

        coEvery { getMovieUseCase.getMovieDetails(any()) } returns mockMovieDetails

        testViewModel.onRetry()
        advanceUntilIdle()

        testViewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isNull()
            assertThat(state.movieId).isEqualTo(mockMovieDetails.id)
        }
        testViewModel.viewModelScope.cancel()
    }

    @Test
    fun `initialization should add movie to recent watched and viewed`() = runTest {
        advanceUntilIdle()

        coVerify { manageRecentMovieWatchedUseCase.addMovieToRecentWatched(any()) }
        coVerify { manageRecentViewedUseCase.addToRecentViewed(any()) }
    }

    @Test
    fun `getMovieId should return default value 0 when movieId is null in savedStateHandle`() =
        runTest {
            every { savedStateHandle.getArgs<Screen.MovieDetails>() } returns null
            val testViewModel = createViewModel()

            assertThat(testViewModel.getMovieId()).isEqualTo(0)
            advanceUntilIdle()

            coVerify { getMovieUseCase.getMovieDetails(0) }
            testViewModel.viewModelScope.cancel()
        }

    @Test
    fun `getMovieImages should use poster as fallback when returns empty list`() = runTest {
        coEvery { getMovieUseCase.getMovieImages(any()) } returns emptyList()
        val testViewModel = createViewModel()
        advanceUntilIdle()

        // Add delay for IO dispatcher
        delay(50)

        testViewModel.state.test {
            val state = awaitItem()
            assertThat(state.movieImages).containsExactly(mockMovieDetails.posterUrl)
        }
        testViewModel.viewModelScope.cancel()
    }

    @Test
    fun `should show error and reset success state ,when rating fails`() = runTest {
        val testRating = 8
        coEvery {
            ratingUseCase.addMovieRatingById(
                any(),
                testRating
            )
        } throws Exception("Rating failed")

        viewModel?.onSelectRatingClick(testRating)
        advanceUntilIdle()

        viewModel?.state?.test {
            val state = awaitItem()
            assertThat(state.isSuccessfullyRated).isNull()
            assertThat(state.isLoading).isFalse()
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

