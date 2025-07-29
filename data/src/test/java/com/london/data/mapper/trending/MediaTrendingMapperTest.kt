package com.london.data.mapper.trending

import com.london.data.remote.model.home.model.trending.TrendingResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaTrendingMapperTest {

    @Test
    fun `toMediaTrending should map TrendingResponse with title to Trending`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 123,
            title = "Test Movie",
            posterPath = "test_poster.jpg",
            genreIds = listOf(28, 12)
        )

        // When
        val result = trendingResponse.toMediaTrending()

        // Then
        assertEquals(123, result.id)
        assertEquals("Test Movie", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.posterPath)
        assertEquals(listOf(28, 12), result.genreIds)
    }

    @Test
    fun `toMediaTrending should map TrendingResponse with name to Trending when title is null`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 456,
            name = "Test TV Show",
            posterPath = "test_poster.jpg",
            genreIds = listOf(18, 35)
        )

        // When
        val result = trendingResponse.toMediaTrending()

        // Then
        assertEquals(456, result.id)
        assertEquals("Test TV Show", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.posterPath)
        assertEquals(listOf(18, 35), result.genreIds)
    }

    @Test
    fun `toMediaTrending should map TrendingResponse with profilePath when posterPath is null`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 789,
            title = "Test Movie",
            profilePath = "test_profile.jpg",
            genreIds = listOf(28, 12)
        )

        // When
        val result = trendingResponse.toMediaTrending()

        // Then
        assertEquals(789, result.id)
        assertEquals("Test Movie", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500test_profile.jpg", result.posterPath)
        assertEquals(listOf(28, 12), result.genreIds)
    }

    @Test
    fun `toMediaTrending should handle null values correctly`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = null,
            title = null,
            name = null,
            posterPath = null,
            profilePath = null,
            genreIds = null
        )

        // When
        val result = trendingResponse.toMediaTrending()

        // Then
        assertEquals(0, result.id)
        assertEquals("", result.title)
        assertEquals("", result.posterPath)
        assertEquals(emptyList<Int>(), result.genreIds)
    }

    @Test
    fun `toMediaTrending should prioritize title over name when both are present`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 123,
            title = "Test Movie",
            name = "Test TV Show",
            posterPath = "test_poster.jpg",
            genreIds = listOf(28, 12)
        )

        // When
        val result = trendingResponse.toMediaTrending()

        // Then
        assertEquals("Test Movie", result.title)
    }

    @Test
    fun `toMediaTrending should prioritize posterPath over profilePath when both are present`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 123,
            title = "Test Movie",
            posterPath = "test_poster.jpg",
            profilePath = "test_profile.jpg",
            genreIds = listOf(28, 12)
        )

        // When
        val result = trendingResponse.toMediaTrending()

        // Then
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.posterPath)
    }

    @Test
    fun `toMediaTrending should handle empty string values`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 123,
            title = "",
            name = "",
            posterPath = "",
            profilePath = "",
            genreIds = emptyList()
        )

        // When
        val result = trendingResponse.toMediaTrending()

        // Then
        assertEquals(123, result.id)
        assertEquals("", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500", result.posterPath)
        assertEquals(emptyList<Int>(), result.genreIds)
    }

    @Test
    fun `toMediaTrending should handle zero id value`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 0,
            title = "Test Movie",
            posterPath = "test_poster.jpg",
            genreIds = listOf(28, 12)
        )

        // When
        val result = trendingResponse.toMediaTrending()

        // Then
        assertEquals(0, result.id)
        assertEquals("Test Movie", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.posterPath)
        assertEquals(listOf(28, 12), result.genreIds)
    }
} 