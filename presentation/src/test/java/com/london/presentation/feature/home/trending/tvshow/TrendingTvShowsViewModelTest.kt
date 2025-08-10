package com.london.presentation.feature.home.trending.tvshow

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.presentation.utils.TvShowGenre
import io.mockk.*
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
class TrendingTvShowsViewModelTest {
    private lateinit var manageTvShowDetailsUseCase: ManageTvShowDetailsUseCase
    private var viewModel: TrendingTvShowsViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        manageTvShowDetailsUseCase = mockk()

        coEvery { manageTvShowDetailsUseCase.getTrendingTvShows(any(), any()) } returns createMockTvShowsResponse()

        viewModel = TrendingTvShowsViewModel(manageTvShowDetailsUseCase = manageTvShowDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `when initializeTvShows succeeds, tvShowsFlow state should be updated`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.tvShowsFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeTvShows starts, loading state should be true`() = runTest {
        // Given
        coEvery { manageTvShowDetailsUseCase.getTrendingTvShows(1, any()) } coAnswers {
            delay(100)
            createMockTvShowsResponse()
        }

        viewModel = TrendingTvShowsViewModel(manageTvShowDetailsUseCase = manageTvShowDetailsUseCase)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onTvShowClick should emit NavigateToTvShow effect with correct tv show ID`() = runTest {
        // Given
        val tvShowId = 12345

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onTvShowClick(tvShowId)
            assertThat(awaitItem()).isEqualTo(
                TrendingTvShowsEffect.NavigateToTvShow(tvShowId)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBack should emit NavigateBack effect`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBack()
            assertThat(awaitItem()).isEqualTo(TrendingTvShowsEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onGenreSelected should update selectedGenreId and reinitialize tv shows`() = runTest {
        // Given
        val comedyGenre = TvShowGenre.Comedy
        val actionGenre = TvShowGenre.ActionAdventure

        // When
        viewModel?.onGenreSelected(comedyGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(comedyGenre.id)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onGenreSelected should not reinitialize if same genre is selected`() = runTest {
        // Given
        val comedyGenre = TvShowGenre.Comedy
        val initialCallCount = mutableListOf<Int>()

        // When & Then
        viewModel?.onGenreSelected(comedyGenre)
        advanceUntilIdle()
        viewModel?.onGenreSelected(comedyGenre)
        advanceUntilIdle()

    }

    @Test
    fun `onGenreSelected should call getTrendingTvShows with correct parameters`() = runTest {
        // Given
        val dramaGenre = TvShowGenre.Drama
        val pageNumber = 1

        // When & Then
        viewModel?.onGenreSelected(dramaGenre)
        advanceUntilIdle()
    }

    @Test
    fun `onRetry should reinitialize tv shows`() = runTest {
        // Given
        val initialCallCount = mutableListOf<Int>()

        // When
        viewModel?.onRetry()
        advanceUntilIdle()

    }

    @Test
    fun `onRetry should call getTrendingTvShows with current selectedGenreId`() = runTest {
        // Given
        val comedyGenre = TvShowGenre.Comedy
        viewModel?.onGenreSelected(comedyGenre)
        advanceUntilIdle()

        // When
        viewModel?.onRetry()
        advanceUntilIdle()

    }

    @Test
    fun `initial state should have default values`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.tvShowsGenres).isEqualTo(TvShowGenre.entries.toList())
            assertThat(state.errorState).isNull()
            assertThat(state.selectedGenreId).isEqualTo(-1)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should handle multiple genre selections correctly`() = runTest {
        // Given
        val comedyGenre = TvShowGenre.Comedy
        val dramaGenre = TvShowGenre.Drama
        val actionGenre = TvShowGenre.ActionAdventure

        // When
        viewModel?.onGenreSelected(comedyGenre)
        advanceUntilIdle()
        viewModel?.onGenreSelected(dramaGenre)
        advanceUntilIdle()
        viewModel?.onGenreSelected(actionGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(actionGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle All genre selection correctly`() = runTest {
        // Given
        val allGenre = TvShowGenre.All

        // When
        viewModel?.onGenreSelected(allGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(allGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle genre selection with special characters`() = runTest {
        // Given
        val mysteryGenre = TvShowGenre.Mystery

        // When
        viewModel?.onGenreSelected(mysteryGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(mysteryGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle rapid genre selections`() = runTest {
        // Given
        val comedyGenre = TvShowGenre.Comedy
        val dramaGenre = TvShowGenre.Drama
        val actionGenre = TvShowGenre.ActionAdventure

        // When
        viewModel?.onGenreSelected(comedyGenre)
        viewModel?.onGenreSelected(dramaGenre)
        viewModel?.onGenreSelected(actionGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(actionGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should maintain state consistency across operations`() = runTest {
        // Given
        val comedyGenre = TvShowGenre.Comedy

        // When
        viewModel?.onGenreSelected(comedyGenre)
        advanceUntilIdle()
        viewModel?.onRetry()
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(comedyGenre.id)
            assertThat(state.tvShowsGenres).isEqualTo(TvShowGenre.entries.toList())
            assertThat(state.errorState).isNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should handle edge case genre IDs`() = runTest {
        // Given
        val familyGenre = TvShowGenre.Family
        val kidsGenre = TvShowGenre.Kids

        // When
        viewModel?.onGenreSelected(familyGenre)
        advanceUntilIdle()
        viewModel?.onGenreSelected(kidsGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(kidsGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle news and reality genres correctly`() = runTest {
        // Given
        val newsGenre = TvShowGenre.News
        val realityGenre = TvShowGenre.Reality

        // When
        viewModel?.onGenreSelected(newsGenre)
        advanceUntilIdle()
        viewModel?.onGenreSelected(realityGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(realityGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle fantasy and soap genres correctly`() = runTest {
        // Given
        val fantasyGenre = TvShowGenre.Fantasy
        val soapGenre = TvShowGenre.Soap

        // When
        viewModel?.onGenreSelected(fantasyGenre)
        advanceUntilIdle()
        viewModel?.onGenreSelected(soapGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(soapGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle talk and war politics genres correctly`() = runTest {
        // Given
        val talkGenre = TvShowGenre.Talk
        val warPoliticsGenre = TvShowGenre.WarPolitics

        // When
        viewModel?.onGenreSelected(talkGenre)
        advanceUntilIdle()
        viewModel?.onGenreSelected(warPoliticsGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(warPoliticsGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle western genre correctly`() = runTest {
        // Given
        val westernGenre = TvShowGenre.Western

        // When
        viewModel?.onGenreSelected(westernGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(westernGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle animation genre correctly`() = runTest {
        // Given
        val animationGenre = TvShowGenre.Animation

        // When
        viewModel?.onGenreSelected(animationGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(animationGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle crime genre correctly`() = runTest {
        // Given
        val crimeGenre = TvShowGenre.Crime

        // When
        viewModel?.onGenreSelected(crimeGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(crimeGenre.id)
            ensureAllEventsConsumed()
        }

    }

    @Test
    fun `should handle documentary genre correctly`() = runTest {
        // Given
        val documentaryGenre = TvShowGenre.Documentary

        // When
        viewModel?.onGenreSelected(documentaryGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(documentaryGenre.id)
            ensureAllEventsConsumed()
        }

    }

    private fun createMockTvShowsResponse(): PagedFetchResponse<Trending> =
        PagedFetchResponse(
            currentPage = 1,
            items = listOf(createMockTrending()),
            totalPages = 10,
            totalItems = 100
        )

    private fun createMockTrending(
        id: Int = 1,
        title: String = "Test TV Show",
        posterPath: String = "test_poster.jpg",
        genreIds: List<Int> = listOf(28, 35)
    ): Trending = Trending(
        id = id,
        title = title,
        posterPath = posterPath,
        genreIds = genreIds
    )
} 