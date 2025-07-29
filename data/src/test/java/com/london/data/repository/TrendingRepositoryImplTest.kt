package com.london.data.repository

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.trending.TrendingResponse
import com.london.data.remote.source.home.trending.TrendingRemoteDataSource
import com.london.data.repository.trending.TrendingRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class TrendingRepositoryImplTest {

    private lateinit var repository: TrendingRepositoryImpl
    private lateinit var remoteDataSource: TrendingRemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk()
        repository = TrendingRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getTrendingMovies should return mapped trending movies`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingMoviesApiResponse()
        coEvery { remoteDataSource.getTrendingMovies(any()) } returns Result.success(mockApiResponse)

        // When
        val result = repository.getTrendingMovies(page = 1)

        // Then
        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(10, result.totalPages)
        assertEquals(100, result.totalItems)
        assertEquals(1, result.items.size)

        val trending = result.items.first()
        assertEquals(1, trending.id)
        assertEquals("Test Movie", trending.title)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", trending.posterPath)
        assertEquals(listOf(28, 12), trending.genreIds)
    }

    @Test
    fun `getTrendingTvShows should return mapped trending tv shows`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingTvShowsApiResponse()
        coEvery { remoteDataSource.getTrendingTvShows(any()) } returns Result.success(
            mockApiResponse
        )

        // When
        val result = repository.getTrendingTvShows(page = 1)

        // Then
        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(10, result.totalPages)
        assertEquals(100, result.totalItems)
        assertEquals(1, result.items.size)

        val trending = result.items.first()
        assertEquals(1, trending.id)
        assertEquals("Test TV Show", trending.title)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", trending.posterPath)
        assertEquals(listOf(18, 35), trending.genreIds)
    }

    @Test
    fun `getTrendingActors should return mapped trending actors`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingActorsApiResponse()
        coEvery { remoteDataSource.getTrendingActors(any()) } returns Result.success(mockApiResponse)

        // When
        val result = repository.getTrendingActors(page = 1)

        // Then
        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(10, result.totalPages)
        assertEquals(100, result.totalItems)
        assertEquals(1, result.items.size)

        val actor = result.items.first()
        assertEquals(1, actor.id)
        assertEquals("Test Actor", actor.name)
        assertEquals("https://image.tmdb.org/t/p/w500test_profile.jpg", actor.profilePicture)
        assertEquals("", actor.characterName)
    }

    @Test
    fun `getTrendingMovies should handle error from remote data source`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery { remoteDataSource.getTrendingMovies(any()) } returns Result.failure(error)

        // When
        try {
            repository.getTrendingMovies(page = 1)
            assert(false)
        } catch (e: Exception) {
            // Then
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `getTrendingTvShows should handle error from remote data source`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery { remoteDataSource.getTrendingTvShows(any()) } returns Result.failure(error)

        // When
        try {
            repository.getTrendingTvShows(page = 1)
            assert(false)
        } catch (e: Exception) {
            // Then
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `getTrendingActors should handle error from remote data source`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery { remoteDataSource.getTrendingActors(any()) } returns Result.failure(error)

        // When
        try {
            repository.getTrendingActors(page = 1)
            assert(false)
        } catch (e: Exception) {
            // Then
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `getTrendingMovies should handle pagination correctly`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingMoviesApiResponse()
        coEvery { remoteDataSource.getTrendingMovies(any()) } returns Result.success(mockApiResponse)

        // When
        val result1 = repository.getTrendingMovies(page = 1)
        val result2 = repository.getTrendingMovies(page = 2)

        // Then
        assertNotNull(result1)
        assertNotNull(result2)
        assertEquals(1, result1.currentPage)
        assertEquals(1, result2.currentPage)
    }

    @Test
    fun `getTrendingTvShows should handle pagination correctly`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingTvShowsApiResponse()
        coEvery { remoteDataSource.getTrendingTvShows(any()) } returns Result.success(
            mockApiResponse
        )

        // When
        val result1 = repository.getTrendingTvShows(page = 1)
        val result2 = repository.getTrendingTvShows(page = 2)

        // Then
        assertNotNull(result1)
        assertNotNull(result2)
        assertEquals(1, result1.currentPage)
        assertEquals(1, result2.currentPage)
    }

    @Test
    fun `getTrendingActors should handle pagination correctly`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingActorsApiResponse()
        coEvery { remoteDataSource.getTrendingActors(any()) } returns Result.success(mockApiResponse)

        // When
        val result1 = repository.getTrendingActors(page = 1)
        val result2 = repository.getTrendingActors(page = 2)

        // Then
        assertNotNull(result1)
        assertNotNull(result2)
        assertEquals(1, result1.currentPage)
        assertEquals(1, result2.currentPage)
    }

    private fun createMockTrendingMoviesApiResponse(): ApiResponse<TrendingResponse> {
        val mockTrendingItem = TrendingResponse(
            id = 1,
            title = "Test Movie",
            posterPath = "test_poster.jpg",
            genreIds = listOf(28, 12)
        )

        return ApiResponse(
            currentPage = 1,
            items = listOf(mockTrendingItem),
            totalPages = 10,
            totalItems = 100
        )
    }

    private fun createMockTrendingTvShowsApiResponse(): ApiResponse<TrendingResponse> {
        val mockTrendingItem = TrendingResponse(
            id = 1,
            name = "Test TV Show",
            posterPath = "test_poster.jpg",
            genreIds = listOf(18, 35)
        )

        return ApiResponse(
            currentPage = 1,
            items = listOf(mockTrendingItem),
            totalPages = 10,
            totalItems = 100
        )
    }

    private fun createMockTrendingActorsApiResponse(): ApiResponse<TrendingResponse> {
        val mockTrendingItem = TrendingResponse(
            id = 1,
            name = "Test Actor",
            profilePath = "test_profile.jpg"
        )

        return ApiResponse(
            currentPage = 1,
            items = listOf(mockTrendingItem),
            totalPages = 10,
            totalItems = 100
        )
    }
} 