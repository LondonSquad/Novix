package com.london.presentation.feature.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.UpComingMovie
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.toDomain
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
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getMovieUseCase = mockk<GetMovieUseCase>()
    private val getTvShowUseCase = mockk<GetTvShowUseCase>()
    private val manageRecentMovieWatchedUseCase = mockk<ManageRecentMovieWatchedUseCase>()
    private val manageRecentTvShowWatchedUseCase = mockk<ManageRecentTvShowWatchedUseCase>()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {

        Dispatchers.setMain(testDispatcher)

        coEvery { getMovieUseCase.getPopularMovies() } returns emptyList()
        coEvery { getTvShowUseCase.getPopularTvShows() } returns emptyList()
        coEvery { getMovieUseCase.getMostRecentMovies() } returns emptyList()
        coEvery { getTvShowUseCase.getMostRecentTvShows() } returns emptyList()
        coEvery { manageRecentMovieWatchedUseCase.getMostRecent() } returns flowOf(emptyList())
        coEvery { manageRecentTvShowWatchedUseCase.getMostRecent() } returns flowOf(emptyList())
        coEvery {
            getMovieUseCase.getUpcomingMoviesByGenre(
                any(),
                any()
            )
        } returns createMockPagedFetchResponse(emptyList())

        viewModel = createViewModel()

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(
            getMovieUseCase = getMovieUseCase,
            getTvShowUseCase = getTvShowUseCase,
            manageRecentMovieWatchedUseCase = manageRecentMovieWatchedUseCase,
            manageRecentTvShowWatchedUseCase = manageRecentTvShowWatchedUseCase
        )
    }

    @Test
    fun `when fetchPopularMediaList, should success updates state correctly`() = runTest {
        // Given
        val mockMovies = listOf(createPopularMovieMedia())
        val mockTvShows = listOf(createPopularTvShowMedia())

        coEvery { getMovieUseCase.getPopularMovies() } returns mockMovies
        coEvery { getTvShowUseCase.getPopularTvShows() } returns mockTvShows

        // When
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.error).isNull()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `when fetchTopRatedMedia, should success updates state with combined data`() = runTest {
        // Given
        val mockTopRatedMovies = listOf(
            createMockTopRatedMedia(1, MediaType.Movie),
            createMockTopRatedMedia(2, MediaType.Movie)
        )
        val mockTopRatedTvShows = listOf(
            createMockTopRatedMedia(3, MediaType.TvShow),
            createMockTopRatedMedia(4, MediaType.TvShow)
        )

        coEvery { getMovieUseCase.getMostRecentMovies() } returns mockTopRatedMovies
        coEvery { getTvShowUseCase.getMostRecentTvShows() } returns mockTopRatedTvShows

        // When
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.isPopularLoading).isFalse()
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `when loadUpcomingMoviesClick, should updates selectedCategoryFlow and loads upcoming movies`() =
        runTest {
            // Given
            val category = MovieGenreUi.Action
            val mockUpcomingMovies = listOf(createMockUpComingMovie(1), createMockUpComingMovie(2))
            val mockResponse = createMockPagedFetchResponse(mockUpcomingMovies)

            coEvery {
                getMovieUseCase.getUpcomingMoviesByGenre(category.toDomain(), 1)
            } returns mockResponse
            advanceUntilIdle()

            // When
            viewModel.loadUpcomingMoviesClick(category)

            // Then
            viewModel.state.test {
                val state = expectMostRecentItem()
                assertThat(state.selectedCategoryFlow.value).isEqualTo(category)
            }
        }

    @Test
    fun `when onMovieGenreSelect, should updates selectedMovieGenre and triggers upcoming movies load`() =
        runTest {
            // Given
            advanceUntilIdle()
            val genre = MovieGenreUi.Action

            // When
            viewModel.onMovieGenreSelect(genre)

            // Then
            viewModel.state.test {
                val state = expectMostRecentItem()
                assertThat(state.selectedMovieGenre).isEqualTo(genre)
            }
        }

    @Test
    fun `when onMovieGenreSelect with All genre, should sets categoryId to null`() = runTest {
        // Given
        advanceUntilIdle()

        // When
        viewModel.onMovieGenreSelect(MovieGenreUi.All)

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedMovieGenre).isEqualTo(MovieGenreUi.All)
        }
    }

    @Test
    fun `when onMovieGenreSelect with same genre, should does not update state`() = runTest {
        // Given
        advanceUntilIdle()
        val initialGenre = viewModel.state.value.selectedMovieGenre

        // When
        viewModel.onMovieGenreSelect(initialGenre)

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedMovieGenre).isEqualTo(initialGenre)
        }
    }

    @Test
    fun `when onMovieClick, should emits NavigationMovieDetails effect`() = runTest {
        // Given
        val movieId = 123

        // When & Then
        viewModel.effect.test {
            viewModel.onMovieClick(movieId)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeScreenEffect.NavigationMovieDetails::class.java)
        }
    }

    @Test
    fun `when onTvShowClick, should emits NavigationTvShowDetails effect`() = runTest {
        // Given
        val tvShowId = 456

        // When & Then
        viewModel.effect.test {
            viewModel.onTvShowClick(tvShowId)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeScreenEffect.NavigationTvShowDetails::class.java)
        }
    }

    @Test
    fun `when onTopRatedClick, should emits NavigationTopRated effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onTopRatedClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeScreenEffect.NavigationTopRated::class.java)
        }
    }

    @Test
    fun `when onContinueWatchingClick, should emits NavigationContinueWatching effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onContinueWatchingClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeScreenEffect.NavigationContinueWatching::class.java)
        }
    }

    @Test
    fun `when onTrendingMoviesCardClick, should emits NavigationTrendingMovie effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onTrendingMoviesCardClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeScreenEffect.NavigationTrendingMovie::class.java)
        }
    }

    @Test
    fun `when onTrendingTvShowsCardClick, emits NavigationTrendingTvShows effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onTrendingTvShowsCardClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeScreenEffect.NavigationTrendingTvShows::class.java)
        }
    }

    @Test
    fun `when onTrendingActorsCardClick, emits NavigationTrendingActor effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onTrendingActorsCardClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeScreenEffect.NavigationTrendingActor::class.java)
        }
    }

    @Test
    fun `when upcoming movies flow, should be initialized properly`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.upcomingMovies).isNotNull()
            assertThat(state.selectedCategoryFlow).isNotNull()
        }
    }

    private fun createPopularMovieMedia() = mockk<PopularMedia> {
        every { id } returns 1
        every { name } returns "Popular Movie"
        every { rating } returns 8.5
        every { posterUrl } returns "/poster1.jpg"
        every { mediaType } returns MediaType.Movie
    }

    private fun createPopularTvShowMedia() = mockk<PopularMedia> {
        every { id } returns 2
        every { name } returns "Popular TV Show"
        every { rating } returns 9.0
        every { posterUrl } returns "/poster2.jpg"
        every { mediaType } returns MediaType.TvShow
    }

    private fun createMockTopRatedMedia(id: Int, mediaType: MediaType) = mockk<TopRatedMedia> {
        every { this@mockk.id } returns id
        every { name } returns "Top Rated $id"
        every { posterUrl } returns "/poster$id.jpg"
        every { genres } returns listOf(MovieGenre.ACTION)
        every { this@mockk.mediaType } returns mediaType
    }

    private fun createMockUpComingMovie(id: Int) = mockk<UpComingMovie> {
        every { this@mockk.id } returns id
        every { imageUrl } returns "/poster$id.jpg"
        every { genres } returns listOf(MovieGenre.ACTION, MovieGenre.ACTION)
    }

    private fun <T> createMockPagedFetchResponse(data: List<T>) = mockk<PagedFetchResponse<T>> {
        every { currentPage } returns 1
        every { items } returns data
        every { totalPages } returns 1
        every { totalItems } returns data.size
    }
}