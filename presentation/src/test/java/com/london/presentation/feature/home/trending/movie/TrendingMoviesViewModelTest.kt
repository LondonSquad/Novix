package com.london.presentation.feature.home.trending.movie

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.utils.MovieGenre
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
    private val getTrendingMovies: GetTrendingMoviesUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getTrendingMovies.invoke(any(), any()) } returns createMockPagedFetchResponse(emptyList())
        viewModel = createViewModel()
    }

    private fun createViewModel() = TrendingMoviesViewModel(getTrendingMovies)

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when creating mock movie, should return correct values`() = runTest {

        //Given
        coEvery {
            getTrendingMovies.invoke(
                page = any(),
                movieGenreId = any()
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
    fun `when click onBack, should emits NavigateBack effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingMoviesEffect.NavigateBack::class.java)
        }
    }
    
    @Test
    fun `when onMovieClick, should emits NavigateToMovie effect`() = runTest {

        //Given 
        val movieId = 1
        
        // When & Then
        viewModel.effect.test {
            viewModel.onMovieClick(movieId)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingMoviesEffect.NavigateToMovie::class.java)
        }
    }
    @Test
    fun `when onRetryClick is called, should update state successfully`() = runTest {

        // Given
        val movie = createMockMovie()
        coEvery { getTrendingMovies.invoke(any(), any()) } returns
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
    fun `when click onGenre, should update selectedGenreId and reload trending movies`() = runTest {

        // Given
        val movieGenre = MovieGenre.Action
        // When
        viewModel.onGenreClick(movieGenre)
        
        // Then
        assertThat(viewModel.state.value.selectedGenreId).isEqualTo(movieGenre.id)
    }

    @Test
    fun `when fetching trending movies and an error occurs,should update error state and loading state correctly`() = runTest {

        viewModel.handlingErrorState(ErrorState.NoInternet)

        // When & Then
        viewModel.state.test {
            viewModel.onRetryClick()
            val state = expectMostRecentItem()
            assertThat(state.errorState).isNotNull()
            assertThat(state.isLoading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }
    private fun createMockMovie() = mockk<Trending> {
        every { id } returns 1
        every { title } returns "Movie Title"
        every { posterPath } returns "https://example.com/poster.jpg"
        every { genreIds } returns listOf(1, 2, 3)
    }

    private fun createMockPagedFetchResponse(data: List<Trending>) =
        mockk<PagedFetchResponse<Trending>> {
            every { currentPage } returns 1
            every { items } returns data
            every { totalPages } returns 1
            every { totalItems } returns data.size
        }
}