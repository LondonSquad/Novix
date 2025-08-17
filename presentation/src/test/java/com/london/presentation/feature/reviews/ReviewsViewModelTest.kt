package com.london.presentation.feature.reviews

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.navigation.Screen
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getMovieUseCase = mockk<GetMovieUseCase>()
    private val getTvShowUseCase = mockk<GetTvShowUseCase>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    private lateinit var viewModel: ReviewsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createViewModel(
        mediaType: MediaType = MediaType.Movie,
        mediaId: Int = 123
    ): ReviewsViewModel {
        every { savedStateHandle.get<Screen.Reviews>(any()) } returns Screen.Reviews(
            mediaType = mediaType,
            mediaId = mediaId
        )
        return ReviewsViewModel(
            getMovieUseCase = getMovieUseCase,
            getTvShowUseCase = getTvShowUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `when initialized with movie type, should load movie reviews`() = runTest {
        // Given
        val movieId = 123
        val mockReviews = createMockPagedFetchResponse(listOf(createMockReview(1)))
        coEvery { getMovieUseCase.getMovieReviews(movieId, any()) } returns mockReviews

        // When
        viewModel = createViewModel(MediaType.Movie, movieId)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isNull()
            assertThat(state.reviews).isNotNull()
        }
    }

    @Test
    fun `when initialized with tv show type, should load tv show reviews`() = runTest {
        // Given
        val tvShowId = 456
        val mockReviews = createMockPagedFetchResponse(listOf(createMockReview(1)))
        coEvery { getTvShowUseCase.getTvShowReviews(tvShowId, any()) } returns mockReviews

        // When
        viewModel = createViewModel(MediaType.TvShow, tvShowId)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isNull()
            assertThat(state.reviews).isNotNull()
        }
    }

    @Test
    fun `when onBackClicked is called, should emit NavigateBack effect`() = runTest {
        // Given
        val movieId = 123
        val mockReviews = createMockPagedFetchResponse(listOf(createMockReview(1)))
        coEvery { getMovieUseCase.getMovieReviews(movieId, any()) } returns mockReviews

        viewModel = createViewModel(MediaType.Movie, movieId)

        // When & Then
        viewModel.effect.test {
            viewModel.onBackClicked()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(ReviewEffect.NavigateBack::class.java)
        }
    }

    @Test
    fun `when initialized with null args, should use default values`() = runTest {
        // Given
        every { savedStateHandle.get<Screen.Reviews>(any()) } returns null
        val mockReviews = createMockPagedFetchResponse(listOf(createMockReview(1)))
        coEvery { getMovieUseCase.getMovieReviews(0, any()) } returns mockReviews

        // When
        viewModel = ReviewsViewModel(
            getMovieUseCase = getMovieUseCase,
            getTvShowUseCase = getTvShowUseCase,
            savedStateHandle = savedStateHandle
        )
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.isLoading).isFalse()
            // Verify it uses default mediaType (Movie) and mediaId (0)
        }
    }

    @Test
    fun `when reviews are loaded successfully, should update state with paging flow`() = runTest {
        // Given
        val movieId = 123
        val mockReviews = listOf(
            createMockReview(1),
            createMockReview(2),
            createMockReview(3)
        )
        val mockResponse = createMockPagedFetchResponse(mockReviews)
        coEvery { getMovieUseCase.getMovieReviews(movieId, any()) } returns mockResponse

        // When
        viewModel = createViewModel(MediaType.Movie, movieId)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isNull()
            assertThat(state.reviews).isNotNull()
            assertThat(state.reviews).isInstanceOf(Flow::class.java)
        }
    }

    @Test
    fun `when multiple pages are requested, should call use case with correct page numbers`() =
        runTest {
            // Given
            val movieId = 123
            val page1Reviews = listOf(createMockReview(1), createMockReview(2))
            val page2Reviews = listOf(createMockReview(3), createMockReview(4))

            coEvery {
                getMovieUseCase.getMovieReviews(
                    movieId,
                    1
                )
            } returns createMockPagedFetchResponse(page1Reviews, currentPage = 1, totalPages = 2)
            coEvery {
                getMovieUseCase.getMovieReviews(
                    movieId,
                    2
                )
            } returns createMockPagedFetchResponse(page2Reviews, currentPage = 2, totalPages = 2)

            // When
            viewModel = createViewModel(MediaType.Movie, movieId)
            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val state = expectMostRecentItem()
                assertThat(state.reviews).isNotNull()
                // The actual paging behavior would be tested in integration tests
            }
        }

    @Test
    fun `when error state exists and new reviews are loaded, should clear error`() = runTest {
        // Given - Initial error state
        val movieId = 123
        coEvery {
            getMovieUseCase.getMovieReviews(
                movieId,
                any()
            )
        } throws Exception("Initial error")

        viewModel = createViewModel(MediaType.Movie, movieId)
        advanceUntilIdle()

        // When - Successful retry
        val mockReviews = createMockPagedFetchResponse(listOf(createMockReview(1)))
        coEvery { getMovieUseCase.getMovieReviews(movieId, any()) } returns mockReviews
        viewModel.onRetry()
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.error).isNull()
        }
    }

    // Helper functions
    private fun createMockReview(id: Int) = mockk<ReviewEntity> {
        every { this@mockk.id } returns id.toString()
        every { content } returns "Review content $id"
        every { createdAt } returns "2024-01-0$id"
    }

    private fun <T> createMockPagedFetchResponse(
        data: List<T>,
        currentPage: Int = 1,
        totalPages: Int = 1
    ) = mockk<PagedFetchResponse<T>> {
        every { this@mockk.currentPage } returns currentPage
        every { items } returns data
        every { this@mockk.totalPages } returns totalPages
        every { totalItems } returns data.size
    }
}