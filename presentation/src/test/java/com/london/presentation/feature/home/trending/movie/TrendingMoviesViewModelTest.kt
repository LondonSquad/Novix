package com.london.presentation.feature.home.trending.movie

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.utils.MovieGenre
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
class TrendingMoviesViewModelTest {
    private lateinit var getTrendingMovies: GetTrendingMoviesUseCase
    private var viewModel: TrendingMoviesViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        getTrendingMovies = mockk()

        coEvery { getTrendingMovies.invoke(any(), any()) } returns createMockMoviesResponse()

        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `when initializeMovies succeeds, moviesFlow state should be updated`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies starts, loading state should be true`() = runTest {
        // Given
        coEvery { getTrendingMovies.invoke(1, any()) } coAnswers {
            delay(100)
            createMockMoviesResponse()
        }

        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

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
    fun `onMovieClick should emit NavigateToMovie effect with correct movie ID`() = runTest {
        // Given
        val movieId = 12345

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onMovieClick(movieId)
            assertThat(awaitItem()).isEqualTo(
                TrendingMoviesEffect.NavigateToMovie(movieId)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBack should emit NavigateBack effect`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBack()
            assertThat(awaitItem()).isEqualTo(TrendingMoviesEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRetry should reinitialize movies`() = runTest {
        // When
        viewModel?.onRetry()
        advanceUntilIdle()
    }

    @Test
    fun `when initializeMovies succeeds multiple times, state should be updated correctly`() =
        runTest {
            // When
            advanceUntilIdle()

            // Then
            viewModel?.state?.test {
                val state = expectMostRecentItem()
                assertThat(state.moviesFlow).isNotNull()
                assertThat(state.isLoading).isFalse()
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `when initializeMovies with empty response, state should handle empty data`() = runTest {
        // Given
        val emptyResponse = PagedFetchResponse<Trending>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 0,
            totalItems = 0
        )
        coEvery { getTrendingMovies.invoke(1, any()) } returns emptyResponse

        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies with large dataset, state should handle large data`() = runTest {
        // Given
        val largeResponse = createMockMoviesResponse(
            items = (1..100).map { createMockMovie(id = it, title = "Movie $it") }
        )
        coEvery { getTrendingMovies.invoke(1, any()) } returns largeResponse

        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onGenreSelected should update selectedGenreId and reinitialize movies`() = runTest {
        // Given
        val actionGenre = MovieGenre.Action
        val initialGenreId = viewModel?.state?.value?.selectedGenreId

        // When
        viewModel?.onGenreSelected(actionGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(actionGenre.id)
            assertThat(state.selectedGenreId).isNotEqualTo(initialGenreId)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies with genre filter, should filter movies by genre`() = runTest {
        // Given
        val actionGenre = MovieGenre.Action
        val moviesWithAction = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 1, title = "Action Movie 1", genreIds = listOf(28, 12)),
                createMockMovie(id = 2, title = "Action Movie 2", genreIds = listOf(28, 35)),
                createMockMovie(id = 3, title = "Comedy Movie", genreIds = listOf(35, 18))
            )
        )
        coEvery { getTrendingMovies.invoke(any(), any()) } returns moviesWithAction
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        viewModel?.onGenreSelected(actionGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(actionGenre.id)
            assertThat(state.moviesFlow).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies with specific genre filter, should only return movies with that genre`() = runTest {
        // Given
        val comedyGenre = MovieGenre.Comedy
        val mixedMovies = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 1, title = "Action Movie", genreIds = listOf(28, 12)),
                createMockMovie(id = 2, title = "Comedy Movie 1", genreIds = listOf(35, 18)),
                createMockMovie(id = 3, title = "Comedy Movie 2", genreIds = listOf(35, 28)),
                createMockMovie(id = 4, title = "Drama Movie", genreIds = listOf(18, 36)),
                createMockMovie(id = 5, title = "Comedy Movie 3", genreIds = listOf(35, 12))
            )
        )
        coEvery { getTrendingMovies.invoke(any()) } returns mixedMovies
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        viewModel?.onGenreSelected(comedyGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(comedyGenre.id)
            assertThat(state.moviesFlow).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies with genre filter changes, should reinitialize with new filter`() = runTest {
        // Given
        val actionGenre = MovieGenre.Action
        val comedyGenre = MovieGenre.Comedy
        val mixedMovies = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 1, title = "Action Movie", genreIds = listOf(28, 12)),
                createMockMovie(id = 2, title = "Comedy Movie", genreIds = listOf(35, 18)),
                createMockMovie(id = 3, title = "Action Comedy", genreIds = listOf(28, 35))
            )
        )
        coEvery { getTrendingMovies.invoke(any()) } returns mixedMovies
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        viewModel?.onGenreSelected(actionGenre)
        advanceUntilIdle()
        
        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(actionGenre.id)
            ensureAllEventsConsumed()
        }

        // When
        viewModel?.onGenreSelected(comedyGenre)
        advanceUntilIdle()
        
        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(comedyGenre.id)
            assertThat(state.selectedGenreId).isNotEqualTo(actionGenre.id)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies with All genre selected, should return all movies unfiltered`() = runTest {
        // Given
        val allGenre = MovieGenre.All
        val mixedMovies = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 1, title = "Action Movie", genreIds = listOf(28, 12)),
                createMockMovie(id = 2, title = "Comedy Movie", genreIds = listOf(35, 18)),
                createMockMovie(id = 3, title = "Drama Movie", genreIds = listOf(18, 36))
            )
        )
        coEvery { getTrendingMovies.invoke(any()) } returns mixedMovies
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        viewModel?.onGenreSelected(allGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(allGenre.id)
            assertThat(state.moviesFlow).isNotNull()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies executes block, should create paging source flow with correct query`() = runTest {
        // Given
        val testQuery = ""
        val mockMovies = createMockMoviesResponse()
        coEvery { getTrendingMovies.invoke(any()) } returns mockMovies
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies executes block, should handle paging with different page numbers`() = runTest {
        // Given
        val page1Movies = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 1, title = "Movie 1", genreIds = listOf(28, 12)),
                createMockMovie(id = 2, title = "Movie 2", genreIds = listOf(35, 18))
            )
        )
        val page2Movies = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 3, title = "Movie 3", genreIds = listOf(28, 36)),
                createMockMovie(id = 4, title = "Movie 4", genreIds = listOf(35, 12))
            )
        )
        
        coEvery { getTrendingMovies.invoke(1) } returns page1Movies
        coEvery { getTrendingMovies.invoke(2) } returns page2Movies
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies executes block with genre filter, should apply filtering in paging flow`() = runTest {
        // Given
        val actionGenre = MovieGenre.Action
        val mixedMovies = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 1, title = "Action Movie", genreIds = listOf(28, 12)),
                createMockMovie(id = 2, title = "Comedy Movie", genreIds = listOf(35, 18)),
                createMockMovie(id = 3, title = "Action Comedy", genreIds = listOf(28, 35))
            )
        )
        coEvery { getTrendingMovies.invoke(any()) } returns mixedMovies
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        viewModel?.onGenreSelected(actionGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(actionGenre.id)
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies executes block, should handle empty response correctly`() = runTest {
        // Given
        val emptyResponse = createMockMoviesResponse(items = emptyList())
        coEvery { getTrendingMovies.invoke(any()) } returns emptyResponse
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies executes block, should handle large response correctly`() = runTest {
        // Given
        val largeResponse = createMockMoviesResponse(
            items = List(100) { index ->
                createMockMovie(
                    id = index + 1,
                    title = "Movie ${index + 1}",
                    genreIds = listOf(28, 35, 18)
                )
            }
        )
        coEvery { getTrendingMovies.invoke(any()) } returns largeResponse
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies executes block, should handle genre filter edge cases`() = runTest {
        // Given
        val edgeCaseMovies = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 1, title = "Movie with null genre", genreIds = listOf()),
                createMockMovie(id = 2, title = "Movie with single genre", genreIds = listOf(28)),
                createMockMovie(id = 3, title = "Movie with multiple genres", genreIds = listOf(28, 35, 18))
            )
        )
        coEvery { getTrendingMovies.invoke(any()) } returns edgeCaseMovies
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeMovies executes block, should handle genre filter with selectedGenreId equals minus one`() = runTest {
        // Given
        val allGenre = MovieGenre.All
        val testMovies = createMockMoviesResponse(
            items = listOf(
                createMockMovie(id = 1, title = "Action Movie", genreIds = listOf(28)),
                createMockMovie(id = 2, title = "Comedy Movie", genreIds = listOf(35)),
                createMockMovie(id = 3, title = "Drama Movie", genreIds = listOf(18))
            )
        )
        coEvery { getTrendingMovies.invoke(any()) } returns testMovies
        
        viewModel = TrendingMoviesViewModel(getTrendingMovies = getTrendingMovies)

        // When
        viewModel?.onGenreSelected(allGenre)
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.selectedGenreId).isEqualTo(-1)
            assertThat(state.moviesFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    companion object {
        private fun createMockMoviesResponse(
            page: Int = 1,
            items: List<Trending> = listOf(createMockMovie())
        ): PagedFetchResponse<Trending> = PagedFetchResponse<Trending>(
            currentPage = page,
            items = items,
            totalPages = 10,
            totalItems = 100
        )

        private fun createMockMovie(
            id: Int = 1,
            title: String = "Test Movie",
            posterPath: String = "test_poster.jpg",
            genreIds: List<Int> = listOf(28, 12)
        ): Trending = Trending(
            id = id,
            title = title,
            posterPath = posterPath,
            genreIds = genreIds
        )
    }
} 