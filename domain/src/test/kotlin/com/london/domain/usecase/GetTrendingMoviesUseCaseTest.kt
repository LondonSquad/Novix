package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetTrendingMoviesUseCaseTest {

    private lateinit var useCase: GetTrendingMoviesUseCase
    private lateinit var repository: MovieRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTrendingMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return trending movies from repository`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result = useCase.invoke(
            page = 1,
            movieGenreId = null
        )

        assertEquals(mockResponse, result)

        val trending = result.items.first()
        assertEquals(listOf(28, 12), trending.genreIds)
    }

    @Test
    fun `invoke should handle different page numbers`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result1 = useCase.invoke(
            page = 1,
            movieGenreId = null
        )
        val result2 = useCase.invoke(
            page = 2,
            movieGenreId = null
        )

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
        coEvery { repository.getTrendingMovies(any()) } returns emptyResponse

        val result = useCase.invoke(
            page = 1,
            movieGenreId = null
        )

        assertEquals(emptyResponse.items.size, result.items.size)
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
        coEvery { repository.getTrendingMovies(any()) } returns multipleMoviesResponse

        val result = useCase.invoke(
            page = 1,
            movieGenreId = null
        )

        assertEquals(multipleMoviesResponse, result)
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        val error = Exception("Repository error")
        coEvery { repository.getTrendingMovies(any()) } throws error

        try {
            useCase.invoke(
                page = 1,
                movieGenreId = null
            )
            assert(false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result = useCase.invoke(
            page = -1,
            movieGenreId = null
        )

        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle zero page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result = useCase.invoke(
            page = 0,
            movieGenreId = null
        )

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle large page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result = useCase.invoke(
            page = 999,
            movieGenreId = null
        )

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