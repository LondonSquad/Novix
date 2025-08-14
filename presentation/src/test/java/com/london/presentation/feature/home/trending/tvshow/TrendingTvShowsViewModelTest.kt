package com.london.presentation.feature.home.trending.tvshow

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.utils.TvShowGenre
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class TrendingTvShowsViewModelTest {

    private lateinit var viewModel: TrendingTvShowsViewModel
    private val mockManageTvShowDetailsUseCase: ManageTvShowDetailsUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery {
            mockManageTvShowDetailsUseCase.getTrendingTvShows(
                any(),
                any()
            )
        } returns createMockPagedFetchResponse(emptyList())
        viewModel = TrendingTvShowsViewModel(mockManageTvShowDetailsUseCase)
    }

    @Test
    fun `when creating mock tvShow, should return correct values`() = runTest {
       val pageNumber = 1
        val movieGenreId = -1
        //Given
        coEvery {
            mockManageTvShowDetailsUseCase.getTrendingTvShows(
                page = pageNumber,
                movieGenreId = movieGenreId
            )
        } returns createMockPagedFetchResponse(listOf(createMockTvShow()))

        //When
        advanceUntilIdle()

        //Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.errorState).isNull()
        }
    }

    @Test
    fun `when click onBack, should emits NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingTvShowsEffect.NavigateBack::class.java)
        }
    }

    @Test
    fun `when click onTvShow, should emits NavigateToTvShow effect`() = runTest {
        val movieId = 1

        viewModel.effect.test {
            viewModel.onTvShowClick(movieId)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingTvShowsEffect.NavigateToTvShow::class.java)
        }
    }

    @Test
    fun `when onRetryClick is called, should update state successfully`() = runTest {
        // Given
        coEvery {
            mockManageTvShowDetailsUseCase.getTrendingTvShows(
                any(),
                any()
            )
        } returns createMockPagedFetchResponse(emptyList())

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
        val tvShowGenre = TvShowGenre.ActionAdventure
        // When
        viewModel.onGenreClick(tvShowGenre)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.selectedGenreId).isEqualTo(tvShowGenre.id)
    }

    @Test
    fun `when fetching trending tv shows and an error occurs,should update error state and loading state correctly`() = runTest {
        viewModel.handlingErrorState(ErrorState.NoInternet)
        advanceUntilIdle()

        // When & Then
        viewModel.state.test {
            viewModel.onRetryClick()
            val state = expectMostRecentItem()
            assertThat(state.errorState).isNotNull()
            assertThat(state.isLoading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createMockTvShow() = mockk<Trending> {
        every { id } returns 1
        every { title } returns "tvShow Title"
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