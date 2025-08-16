package com.london.presentation.feature.home.trending.tvshow

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.shared.genre.toUi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingTvShowsViewModelTest {

    private lateinit var viewModel: TrendingTvShowsViewModel
    private val getTvShowUseCase: GetTvShowUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery {
            getTvShowUseCase.getTrendingTvShows(
                any(),
                any()
            )
        } returns createMockPagedFetchResponse(emptyList())
        viewModel = TrendingTvShowsViewModel(getTvShowUseCase)
    }

    @Test
    fun `when creating mock tvShow, should return correct values`() = runTest {
        
        //Given
        val pageNumber = 1
        coEvery {
            getTvShowUseCase.getTrendingTvShows(
                page = pageNumber,
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
        // When & Then
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingTvShowsEffect.NavigateBack::class.java)
        }
    }

    @Test
    fun `when click onTvShow, should emits NavigateToTvShow effect`() = runTest {
        // Given
        val movieId = 1
        // When & Then
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
            getTvShowUseCase.getTrendingTvShows(
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
        val tvShowGenre = TvShowGenre.ACTION_ADVENTURE
        // When
        viewModel.onGenreClick(tvShowGenre.toUi())
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(tvShowGenre.toUi())
    }

    private fun createMockTvShow() = mockk<Trending> {
        every { id } returns 1
        every { title } returns "tvShow Title"
        every { posterPath } returns "https://example.com/poster.jpg"
        every { genres } returns listOf(TvShowGenre.ACTION_ADVENTURE, TvShowGenre.ALL)
    }

    private fun createMockPagedFetchResponse(data: List<Trending>) =
        mockk<PagedFetchResponse<Trending>> {
            every { currentPage } returns 1
            every { items } returns data
            every { totalPages } returns 1
            every { totalItems } returns data.size
        }
}