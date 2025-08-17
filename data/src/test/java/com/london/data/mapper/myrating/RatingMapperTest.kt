package com.london.data.mapper.myrating

import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.domain.entity.recent.MediaType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RatingMapperTest {

    @Test
    fun `toEntity mapping movie response returns correct rated media`() {
        // Given
        val movieResponse = createSampleMovieResponse()

        // When
        val result = movieResponse.toEntity(mediaType = MediaType.Movie)

        // Then
        assertEquals(123, result.id)
        assertEquals("Test Movie", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500/test-poster.jpg", result.posterPath)
        assertEquals(8, result.rating)
        assertTrue(result.mediaType == MediaType.Movie)
    }

    @Test
    fun `toEntity mapping tv show response returns correct rated media`() {
        // Given
        val tvShowResponse = createSampleTvShowResponse()

        // When
        val result = tvShowResponse.toEntity(mediaType = MediaType.TvShow)

        // Then
        assertEquals(456, result.id)
        assertEquals("Test TV Show", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500/test-poster.jpg", result.posterPath)
        assertEquals(7, result.rating)
        assertFalse(result.mediaType == MediaType.Movie)
    }

    @Test
    fun `toEntity with null values returns default values`() {
        // Given
        val movieResponse = RatingMediaResponse(
            id = 0,
            title = null,
            posterPath = null,
            rating = null
        )

        // When
        val result = movieResponse.toEntity(mediaType = MediaType.Movie)

        // Then
        assertEquals(0, result.id)
        assertEquals("", result.title)
        assertEquals("", result.posterPath)
        assertEquals(0, result.rating)
        assertTrue(result.mediaType == MediaType.Movie)
    }

    @Test
    fun `toEntity with empty poster path returns base image url`() {
        // Given
        val movieResponse = createSampleMovieResponse(posterPath = "")

        // When
        val result = movieResponse.toEntity(mediaType = MediaType.Movie)

        // Then
        assertEquals("https://image.tmdb.org/t/p/w500", result.posterPath)
    }

    @Test
    fun `toEntity with null poster path returns empty string`() {
        // Given
        val movieResponse = createSampleMovieResponse(posterPath = null)

        // When
        val result = movieResponse.toEntity(mediaType = MediaType.Movie)

        // Then
        assertEquals("", result.posterPath)
    }

    @Test
    fun `toEntity tv show with empty poster path returns base image url`() {
        // Given
        val tvShowResponse = createSampleTvShowResponse(posterPath = "")

        // When
        val result = tvShowResponse.toEntity(mediaType = MediaType.TvShow)

        // Then
        assertEquals("https://image.tmdb.org/t/p/w500", result.posterPath)
    }

    @Test
    fun `toEntity tv show with null poster path returns empty string`() {
        // Given
        val tvShowResponse = createSampleTvShowResponse(posterPath = null)

        // When
        val result = tvShowResponse.toEntity(mediaType = MediaType.TvShow)

        // Then
        assertEquals("", result.posterPath)
    }

    @Test
    fun `toEntity with high rating returns correct rating value`() {
        // Given
        val movieResponse = createSampleMovieResponse(rating = 9.5)

        // When
        val result = movieResponse.toEntity(mediaType = MediaType.Movie)

        // Then
        assertEquals(9, result.rating)
        assertTrue(result.mediaType == MediaType.Movie)
    }

    @Test
    fun `toEntity with decimal rating truncates correctly`() {
        // Given
        val movieResponse = createSampleMovieResponse(rating = 6.9)

        // When
        val result = movieResponse.toEntity(mediaType = MediaType.Movie)

        // Then
        assertEquals(6, result.rating)
        assertTrue(result.mediaType == MediaType.Movie)
    }

    companion object {
        private fun createSampleMovieResponse(
            id: Int = 123,
            title: String = "Test Movie",
            posterPath: String? = "/test-poster.jpg",
            rating: Double = 8.5
        ) = RatingMediaResponse(
            id = id,
            title = title,
            posterPath = posterPath,
            rating = rating
        )

        private fun createSampleTvShowResponse(
            id: Int = 456,
            title: String = "Test TV Show",
            posterPath: String? = "/test-poster.jpg",
            rating: Double = 7.5
        ) = RatingMediaResponse(
            id = id,
            title = title,
            posterPath = posterPath,
            rating = rating
        )
    }
} 