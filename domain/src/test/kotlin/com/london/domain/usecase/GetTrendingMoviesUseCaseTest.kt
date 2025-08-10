package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.PopularRepository
import com.london.domain.repository.TrendingRepository
import com.london.domain.repository.discover.DiscoverRepository
import com.london.domain.usecase.details.movie.ManageMovieUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetTrendingMoviesUseCaseTest {

    private lateinit var manageMovieUseCase: ManageMovieUseCase
    private lateinit var trendingRepository: TrendingRepository
    private lateinit var popularRepository: PopularRepository
    private lateinit var discoverRepository: DiscoverRepository
    private lateinit var movieDetailsRepository: MovieDetailsRepository

    @Before
    fun setup() {
        movieDetailsRepository = mockk()
        trendingRepository = mockk()
        popularRepository = mockk()
        discoverRepository = mockk()
        manageMovieUseCase = ManageMovieUseCase(
            movieRepository = movieDetailsRepository,
            trendingRepository = trendingRepository,
            popularRepository = popularRepository,
            discoverRepository = discoverRepository
        )
    }

    @Test
    fun `invoke should return trending movies from repository`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { trendingRepository.getTrendingMovies(any()) } returns mockResponse

        val result = manageMovieUseCase.getTrendingMovies(page = 1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(10, result.totalPages)
        assertEquals(100, result.totalItems)
        assertEquals(1, result.items.size)

        val trending = result.items.first()
        assertEquals(1, trending.id)
        assertEquals("Test Movie", trending.title)
        assertEquals("test_poster.jpg", trending.posterPath)
        assertEquals(listOf(28, 12), trending.genreIds)
    }

    @Test
    fun `invoke should handle different page numbers`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { trendingRepository.getTrendingMovies(any()) } returns mockResponse

        val result1 = manageMovieUseCase.getTrendingMovies(page = 1)
        val result2 = manageMovieUseCase.getTrendingMovies(page = 2)

        assertNotNull(result1)
        assertNotNull(result2)
        assertEquals(1, result1.currentPage)
        assertEquals(1, result2.currentPage)
    }

    @Test
    fun `invoke should handle empty response`() = runTest {
        val emptyResponse = PagedFetchResponse<Trending>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 0,
            totalItems = 0
        )
        coEvery { trendingRepository.getTrendingMovies(any()) } returns emptyResponse

        val result = manageMovieUseCase.getTrendingMovies(page = 1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(0, result.totalPages)
        assertEquals(0, result.totalItems)
        assertEquals(0, result.items.size)
    }

    @Test
    fun `invoke should handle multiple movies in response`() = runTest {
        val multipleMoviesResponse = PagedFetchResponse(
            currentPage = 1,
            items = listOf(
                createMockTrending(id = 1, title = "Movie 1"),
                createMockTrending(id = 2, title = "Movie 2"),
                createMockTrending(id = 3, title = "Movie 3")
            ),
            totalPages = 10,
            totalItems = 100
        )
        coEvery { trendingRepository.getTrendingMovies(any()) } returns multipleMoviesResponse

        val result = manageMovieUseCase.getTrendingMovies(page = 1)

        assertNotNull(result)
        assertEquals(3, result.items.size)
        assertEquals("Movie 1", result.items[0].title)
        assertEquals("Movie 2", result.items[1].title)
        assertEquals("Movie 3", result.items[2].title)
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        val error = Exception("Repository error")
        coEvery { trendingRepository.getTrendingMovies(any()) } throws error

        try {
            manageMovieUseCase.getTrendingMovies(page = 1)
            assert(false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { trendingRepository.getTrendingMovies(any()) } returns mockResponse

        val result = manageMovieUseCase.getTrendingMovies(page = -1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle zero page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { trendingRepository.getTrendingMovies(any()) } returns mockResponse

        val result = manageMovieUseCase.getTrendingMovies(page = 0)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle large page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { trendingRepository.getTrendingMovies(any()) } returns mockResponse

        val result = manageMovieUseCase.getTrendingMovies(page = 999)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    private fun createMockTrendingResponse(): PagedFetchResponse<Trending> =
        PagedFetchResponse(
            currentPage = 1,
            items = listOf(createMockTrending()),
            totalPages = 10,
            totalItems = 100
        )

    private fun createMockTrending(
        id: Int = 1,
        title: String = "Test Movie",
        posterPath: String = "test_poster.jpg",
        genreIds: List<Int> = listOf(28, 12)
    ): Trending {
        return Trending(
            id = id,
            title = title,
            posterPath = posterPath,
            genreIds = genreIds
        )
    }
} 