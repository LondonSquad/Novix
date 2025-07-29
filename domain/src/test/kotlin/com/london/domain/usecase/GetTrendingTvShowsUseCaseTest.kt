package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.TrendingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetTrendingTvShowsUseCaseTest {

    private lateinit var useCase: GetTrendingTvShowsUseCase
    private lateinit var repository: TrendingRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTrendingTvShowsUseCase(repository)
    }

    @Test
    fun `invoke should return trending tv shows from repository`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingTvShows(any()) } returns mockResponse

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(10, result.totalPages)
        assertEquals(100, result.totalItems)
        assertEquals(1, result.items.size)

        val trending = result.items.first()
        assertEquals(1, trending.id)
        assertEquals("Test TV Show", trending.title)
        assertEquals("test_poster.jpg", trending.posterPath)
        assertEquals(listOf(18, 35), trending.genreIds)
    }

    @Test
    fun `invoke should handle different page numbers`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingTvShows(any()) } returns mockResponse

        val result1 = useCase.invoke(page = 1)
        val result2 = useCase.invoke(page = 2)

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
        coEvery { repository.getTrendingTvShows(any()) } returns emptyResponse

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(0, result.totalPages)
        assertEquals(0, result.totalItems)
        assertEquals(0, result.items.size)
    }

    @Test
    fun `invoke should handle multiple tv shows in response`() = runTest {
        val multipleTvShowsResponse = PagedFetchResponse<Trending>(
            currentPage = 1,
            items = listOf(
                createMockTrending(id = 1, title = "TV Show 1"),
                createMockTrending(id = 2, title = "TV Show 2"),
                createMockTrending(id = 3, title = "TV Show 3")
            ),
            totalPages = 10,
            totalItems = 100
        )
        coEvery { repository.getTrendingTvShows(any()) } returns multipleTvShowsResponse

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(3, result.items.size)
        assertEquals("TV Show 1", result.items[0].title)
        assertEquals("TV Show 2", result.items[1].title)
        assertEquals("TV Show 3", result.items[2].title)
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        val error = Exception("Repository error")
        coEvery { repository.getTrendingTvShows(any()) } throws error

        try {
            useCase.invoke(page = 1)
            assert(false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingTvShows(any()) } returns mockResponse

        val result = useCase.invoke(page = -1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle zero page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingTvShows(any()) } returns mockResponse

        val result = useCase.invoke(page = 0)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle large page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingTvShows(any()) } returns mockResponse

        val result = useCase.invoke(page = 999)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle tv shows with different genres`() = runTest {
        val tvShowsWithDifferentGenres = PagedFetchResponse<Trending>(
            currentPage = 1,
            items = listOf(
                createMockTrending(id = 1, title = "Drama Show", genreIds = listOf(18)),
                createMockTrending(id = 2, title = "Comedy Show", genreIds = listOf(35)),
                createMockTrending(id = 3, title = "Action Show", genreIds = listOf(28))
            ),
            totalPages = 10,
            totalItems = 100
        )
        coEvery { repository.getTrendingTvShows(any()) } returns tvShowsWithDifferentGenres

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(3, result.items.size)
        assertEquals(listOf(18), result.items[0].genreIds)
        assertEquals(listOf(35), result.items[1].genreIds)
        assertEquals(listOf(28), result.items[2].genreIds)
    }

    private fun createMockTrendingResponse(): PagedFetchResponse<Trending> {
        return PagedFetchResponse(
            currentPage = 1,
            items = listOf(createMockTrending()),
            totalPages = 10,
            totalItems = 100
        )
    }

    private fun createMockTrending(
        id: Int = 1,
        title: String = "Test TV Show",
        posterPath: String = "test_poster.jpg",
        genreIds: List<Int> = listOf(18, 35)
    ): Trending {
        return Trending(
            id = id,
            title = title,
            posterPath = posterPath,
            genreIds = genreIds
        )
    }
} 