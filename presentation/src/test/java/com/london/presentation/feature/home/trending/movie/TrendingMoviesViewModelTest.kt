package com.london.presentation.feature.home.trending.movie

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.presentation.shared.genre.toUi
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingMoviesViewModelTest {
    private lateinit var viewModel: TrendingMoviesViewModel
    private val getMovieUseCase: GetMovieUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getMovieUseCase.getTrendingMovies(any(), any()) } returns createMockPagedFetchResponse(emptyList())
        viewModel = createViewModel()
    }

    private fun createViewModel() = TrendingMoviesViewModel(getMovieUseCase)

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getTrendingMovies should return initializing mock movie should return correct values`() =
        runTest {

        //Given
        coEvery {
            getMovieUseCase.getTrendingMovies(
                page = any(),
                movieGenre = any()
            )
        } returns createMockPagedFetchResponse(listOf(createMockMovie()))

        //When
        advanceUntilIdle()

        //Then
        viewModel.state.test {
            val actors = expectMostRecentItem().moviesFlow.first()
            assertThat(actors).isInstanceOf(PagingData::class.java)
        }
    }

    @Test
    fun `onBackClick should return emits when NavigateBack effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingMoviesEffect.BackNavigationClick::class.java)
        }
    }
    
    @Test
    fun `onMovieClick should return emits when NavigateToMovie effect`() = runTest {

        //Given 
        val movieId = 1
        
        // When & Then
        viewModel.effect.test {
            viewModel.onMovieClick(movieId)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingMoviesEffect.MovieDetailsNavigation::class.java)
        }
    }
    @Test
    fun `onRetryClick should return updates successfully`() = runTest {

        // Given
        val movie = createMockMovie()
        coEvery { getMovieUseCase.getTrendingMovies(any(), any()) } returns
                createMockPagedFetchResponse(listOf(movie))

        // When
        viewModel.onRetryClick()
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.errorState).isNull()
        }
    }

    @Test
    fun `onGenreClick should return updates selectedGenreId and reload trending movies`() =
        runTest {

        // Given
        val movieGenre = MovieGenre.ACTION
        // When
        viewModel.onGenreClick(movieGenre.toUi())
        
        // Then
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(movieGenre.toUi())
    }
    private fun createMockMovie() = mockk<Trending> {
        every { id } returns 1
        every { title } returns "Movie Title"
        every { posterPath } returns "https://example.com/poster.jpg"
        every { genres } returns listOf(MovieGenre.ACTION, MovieGenre.ACTION)
    }

    private fun createMockPagedFetchResponse(data: List<Trending>) =
        mockk<PagedFetchResponse<Trending>> {
            every { currentPage } returns 1
            every { items } returns data
            every { totalPages } returns 1
            every { totalItems } returns data.size
        }
}