package com.london.data.mapper.trending

import com.london.data.mapper.home.trending.toEntityActor
import com.london.data.remote.model.home.trending.TrendingResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class TrendingActorMapperTest {

    @Test
    fun `toTrendingActor should map TrendingResponse with name and posterPath to Actor`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 123,
            name = "Test Actor",
            posterPath = "test_poster.jpg"
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(123, result.id)
        assertEquals("Test Actor", result.name)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.profilePictureUrl)
        assertEquals("", result.characterName)
    }

    @Test
    fun `toTrendingActor should map TrendingResponse with profilePath when posterPath is null`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 456,
            name = "Test Actor",
            profilePath = "test_profile.jpg"
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(456, result.id)
        assertEquals("Test Actor", result.name)
        assertEquals("https://image.tmdb.org/t/p/w500test_profile.jpg", result.profilePictureUrl)
        assertEquals("", result.characterName)
    }

    @Test
    fun `toTrendingActor should prioritize posterPath over profilePath when both are present`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 789,
            name = "Test Actor",
            posterPath = "test_poster.jpg",
            profilePath = "test_profile.jpg"
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(789, result.id)
        assertEquals("Test Actor", result.name)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.profilePictureUrl)
        assertEquals("", result.characterName)
    }

    @Test
    fun `toTrendingActor should handle null values correctly`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = null,
            name = null,
            posterPath = null,
            profilePath = null
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(0, result.id)
        assertEquals("", result.name)
        assertEquals("", result.profilePictureUrl)
        assertEquals("", result.characterName)
    }

    @Test
    fun `toTrendingActor should handle empty string values`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 123,
            name = "",
            posterPath = "",
            profilePath = ""
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(123, result.id)
        assertEquals("", result.name)
        assertEquals("https://image.tmdb.org/t/p/w500", result.profilePictureUrl)
        assertEquals("", result.characterName)
    }

    @Test
    fun `toTrendingActor should handle zero id value`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 0,
            name = "Test Actor",
            posterPath = "test_poster.jpg"
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(0, result.id)
        assertEquals("Test Actor", result.name)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.profilePictureUrl)
        assertEquals("", result.characterName)
    }

    @Test
    fun `toTrendingActor should handle special characters in name`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 123,
            name = "Test Actor (Special)",
            posterPath = "test_poster.jpg"
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(123, result.id)
        assertEquals("Test Actor (Special)", result.name)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.profilePictureUrl)
        assertEquals("", result.characterName)
    }

    @Test
    fun `toTrendingActor should handle long name`() {
        // Given
        val longName = "This is a very long actor name that might exceed normal length limits"
        val trendingResponse = TrendingResponse(
            id = 123,
            name = longName,
            posterPath = "test_poster.jpg"
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(123, result.id)
        assertEquals(longName, result.name)
        assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", result.profilePictureUrl)
        assertEquals("", result.characterName)
    }

    @Test
    fun `toTrendingActor should handle image path with special characters`() {
        // Given
        val trendingResponse = TrendingResponse(
            id = 123,
            name = "Test Actor",
            posterPath = "test_poster_with_special_chars_123.jpg"
        )

        // When
        val result = trendingResponse.toEntityActor()

        // Then
        assertEquals(123, result.id)
        assertEquals("Test Actor", result.name)
        assertEquals(
            "https://image.tmdb.org/t/p/w500test_poster_with_special_chars_123.jpg",
            result.profilePictureUrl
        )
        assertEquals("", result.characterName)
    }
} 