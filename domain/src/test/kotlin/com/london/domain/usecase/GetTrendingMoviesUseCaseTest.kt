package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.TrendingRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class GetTrendingMoviesUseCaseTest {

    private lateinit var useCase: GetTrendingMoviesUseCase
    private lateinit var repository: TrendingRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTrendingMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return trending movies from repository`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result = useCase.invoke(page = 1)

        assertThat(result).isNotNull()
        assertThat(result.currentPage).isEqualTo(1)
        assertThat(result.totalPages).isEqualTo(10)
        assertThat(result.totalItems).isEqualTo(100)
        assertThat(result.items).hasSize(1)

        val trending = result.items.first()
        assertThat(trending.id).isEqualTo(1)
        assertThat(trending.title).isEqualTo("Test Movie")
        assertThat(trending.posterPath).isEqualTo("test_poster.jpg")
        assertThat(trending.genreIds).isEqualTo(listOf(28, 12))
    }

    @Test
    fun `invoke should handle different page numbers`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result1 = useCase.invoke(page = 1)
        val result2 = useCase.invoke(page = 2)

        assertThat(result1).isNotNull()
        assertThat(result2).isNotNull()
        assertThat(result1.currentPage).isEqualTo(1)
        assertThat(result2.currentPage).isEqualTo(1)
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

        val result = useCase.invoke(page = 1)

        assertThat(result).isNotNull()
        assertThat(result.currentPage).isEqualTo(1)
        assertThat(result.totalPages).isEqualTo(0)
        assertThat(result.totalItems).isEqualTo(0)
        assertThat(result.items).hasSize(0)
    }

    @Test
    fun `invoke should handle multiple movies in response`() = runTest {
        val multipleMoviesResponse = createMockTrendingResponse(
            items = listOf(
                createMockTrending(id = 1, title = "Movie 1"),
                createMockTrending(id = 2, title = "Movie 2"),
                createMockTrending(id = 3, title = "Movie 3")
            )
        )
        coEvery { repository.getTrendingMovies(any()) } returns multipleMoviesResponse

        val result = useCase.invoke(page = 1)

        assertThat(result).isNotNull()
        assertThat(result.items).hasSize(3)
        assertThat(result.items[0].title).isEqualTo("Movie 1")
        assertThat(result.items[1].title).isEqualTo("Movie 2")
        assertThat(result.items[2].title).isEqualTo("Movie 3")
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        val error = Exception("Repository error")
        coEvery { repository.getTrendingMovies(any()) } throws error

        try {
            useCase.invoke(page = 1)
            assert(false) { "Expected exception to be thrown" }
        } catch (e: Exception) {
            assertThat(e.message).isEqualTo("Repository error")
        }
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result = useCase.invoke(page = -1)

        assertThat(result).isNotNull()
        assertThat(result.currentPage).isEqualTo(1)
    }

    @Test
    fun `invoke should handle zero page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result = useCase.invoke(page = 0)

        assertThat(result).isNotNull()
        assertThat(result.currentPage).isEqualTo(1)
    }

    @Test
    fun `invoke should handle large page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { repository.getTrendingMovies(any()) } returns mockResponse

        val result = useCase.invoke(page = 999)

        assertThat(result).isNotNull()
        assertThat(result.currentPage).isEqualTo(1)
    }

    @Test
    fun `invoke should handle movies with different genre combinations`() = runTest {
        val moviesWithDifferentGenres = createMockTrendingResponse(
            items = listOf(
                createMockTrending(id = 1, title = "Action Movie", genreIds = listOf(28)),
                createMockTrending(id = 2, title = "Comedy Movie", genreIds = listOf(35)),
                createMockTrending(id = 3, title = "Drama Movie", genreIds = listOf(18)),
                createMockTrending(id = 4, title = "Action-Comedy", genreIds = listOf(28, 35))
            ),
            totalPages = 1,
            totalItems = 4
        )
        coEvery { repository.getTrendingMovies(any()) } returns moviesWithDifferentGenres

        val result = useCase.invoke(page = 1)

        assertThat(result).isNotNull()
        assertThat(result.items).hasSize(4)
        assertThat(result.items[0].genreIds).isEqualTo(listOf(28))
        assertThat(result.items[1].genreIds).isEqualTo(listOf(35))
        assertThat(result.items[2].genreIds).isEqualTo(listOf(18))
        assertThat(result.items[3].genreIds).isEqualTo(listOf(28, 35))
    }

    @Test
    fun `invoke should handle movies with empty genre list`() = runTest {
        val movieWithNoGenres = createMockTrendingResponse(
            items = listOf(createMockTrending(id = 1, title = "No Genre Movie", genreIds = emptyList())),
            totalPages = 1,
            totalItems = 1
        )
        coEvery { repository.getTrendingMovies(any()) } returns movieWithNoGenres

        val result = useCase.invoke(page = 1)

        assertThat(result).isNotNull()
        assertThat(result.items).hasSize(1)
        assertThat(result.items[0].genreIds).isEmpty()
    }

    @Test
    fun `invoke should handle movies with special characters in title`() = runTest {
        val movieWithSpecialTitle = createMockTrendingResponse(
            items = listOf(createMockTrending(id = 1, title = "Movie: The Sequel (2024)", genreIds = listOf(28, 12))),
            totalPages = 1,
            totalItems = 1
        )
        coEvery { repository.getTrendingMovies(any()) } returns movieWithSpecialTitle

        val result = useCase.invoke(page = 1)

        assertThat(result).isNotNull()
        assertThat(result.items).hasSize(1)
        assertThat(result.items[0].title).isEqualTo("Movie: The Sequel (2024)")
    }

    @Test
    fun `invoke should filter movies by multiple genre IDs correctly`() = runTest {
        // Given
        val mixedGenreMovies = createMockTrendingResponse(
            items = listOf(
                createMockTrending(id = 1, title = "Action Movie", genreIds = listOf(28)),
                createMockTrending(id = 2, title = "Comedy Movie", genreIds = listOf(35)),
                createMockTrending(id = 3, title = "Action-Comedy", genreIds = listOf(28, 35)),
                createMockTrending(id = 4, title = "Comedy-Drama", genreIds = listOf(35, 18)),
                createMockTrending(id = 5, title = "Action-Drama", genreIds = listOf(28, 18))
            ),
            totalPages = 1,
            totalItems = 5
        )
        coEvery { repository.getTrendingMovies(any()) } returns mixedGenreMovies

        // When
        val comedyGenreId = 35
        val result = useCase.invoke(page = 1, genreId = comedyGenreId)
        
        // Then
        assertThat(result).isNotNull()
        assertThat(result.items).hasSize(3)
        
        result.items.forEach { movie ->
            assertThat(movie.genreIds).contains(comedyGenreId)
        }
        
        val movieTitles = result.items.map { it.title }
        assertThat(movieTitles).contains("Comedy Movie")
        assertThat(movieTitles).contains("Action-Comedy")
        assertThat(movieTitles).contains("Comedy-Drama")
        
        assertThat(movieTitles).doesNotContain("Action Movie")
        assertThat(movieTitles).doesNotContain("Action-Drama")
    }

    @Test
    fun `invoke should handle filtering with no matching genres`() = runTest {
        // Given
        val specificGenreMovies = createMockTrendingResponse(
            items = listOf(
                createMockTrending(id = 1, title = "Action Movie", genreIds = listOf(28)),
                createMockTrending(id = 2, title = "Comedy Movie", genreIds = listOf(35)),
                createMockTrending(id = 3, title = "Drama Movie", genreIds = listOf(18))
            ),
            totalPages = 1,
            totalItems = 3
        )
        coEvery { repository.getTrendingMovies(any()) } returns specificGenreMovies

        // When
        val horrorGenreId = 27
        val result = useCase.invoke(page = 1, genreId = horrorGenreId)
        
        // Then
        assertThat(result).isNotNull()
        assertThat(result.items).isEmpty()
    }

    @Test
    fun `invoke should return all movies when no genre ID is specified`() = runTest {
        // Given
        val allMovies = createMockTrendingResponse(
            items = listOf(
                createMockTrending(id = 1, title = "Action Movie", genreIds = listOf(28)),
                createMockTrending(id = 2, title = "Comedy Movie", genreIds = listOf(35)),
                createMockTrending(id = 3, title = "Drama Movie", genreIds = listOf(18)),
                createMockTrending(id = 4, title = "Action-Comedy", genreIds = listOf(28, 35))
            ),
            totalPages = 1,
            totalItems = 4
        )
        coEvery { repository.getTrendingMovies(any()) } returns allMovies

        // When
        val result = useCase.invoke(page = 1)
        
        // Then
        assertThat(result).isNotNull()
        assertThat(result.items).hasSize(4)
        assertThat(result.items).isEqualTo(allMovies.items)
    }

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

    private fun createMockTrendingResponse(
        currentPage: Int = 1,
        items: List<Trending> = listOf(createMockTrending()),
        totalPages: Int = 10,
        totalItems: Int = 100
    ): PagedFetchResponse<Trending> = PagedFetchResponse(
        currentPage = currentPage,
        items = items,
        totalPages = totalPages,
        totalItems = totalItems
    )
} 