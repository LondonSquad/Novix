package com.london.data.mapper.myrating

import com.london.data.remote.model.myrating.RatedMovieResponse
import com.london.data.remote.model.myrating.RatedTvShowResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MyRatingMapperTest {

    @Test
    fun `toEntity mapping movie response returns correct rated media`() {
        // Given
        val movieResponse = createSampleMovieResponse()

        // When
        val result = movieResponse.toEntity(isMovie = true)

        // Then
        assertEquals(123, result.id)
        assertEquals("Test Movie", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500/test-poster.jpg", result.posterPath)
        assertEquals(8, result.rating)
        assertTrue(result.isMovie)
    }

    @Test
    fun `toEntity mapping tv show response returns correct rated media`() {
        // Given
        val tvShowResponse = createSampleTvShowResponse()

        // When
        val result = tvShowResponse.toEntity(isMovie = false)

        // Then
        assertEquals(456, result.id)
        assertEquals("Test TV Show", result.title)
        assertEquals("https://image.tmdb.org/t/p/w500/test-poster.jpg", result.posterPath)
        assertEquals(7, result.rating)
        assertFalse(result.isMovie)
    }

    @Test
    fun `toEntity with null values returns default values`() {
        // Given
        val movieResponse = RatedMovieResponse(
            id = 0,
            adult = false,
            backdropPath = "",
            title = null,
            posterPath = null,
            voteAverage = null,
            rating = null
        )

        // When
        val result = movieResponse.toEntity()

        // Then
        assertEquals(0, result.id)
        assertEquals("", result.title)
        assertEquals("", result.posterPath)
        assertEquals(0, result.rating)
        assertTrue(result.isMovie)
    }

    @Test
    fun `toEntity with empty poster path returns base image url`() {
        // Given
        val movieResponse = createSampleMovieResponse(posterPath = "")

        // When
        val result = movieResponse.toEntity()

        // Then
        assertEquals("https://image.tmdb.org/t/p/w500", result.posterPath)
    }

    @Test
    fun `toEntity with null poster path returns empty string`() {
        // Given
        val movieResponse = createSampleMovieResponse(posterPath = null)

        // When
        val result = movieResponse.toEntity()

        // Then
        assertEquals("", result.posterPath)
    }

    @Test
    fun `toEntity tv show with empty poster path returns base image url`() {
        // Given
        val tvShowResponse = createSampleTvShowResponse(posterPath = "")

        // When
        val result = tvShowResponse.toEntity()

        // Then
        assertEquals("https://image.tmdb.org/t/p/w500", result.posterPath)
    }

    @Test
    fun `toEntity tv show with null poster path returns empty string`() {
        // Given
        val tvShowResponse = createSampleTvShowResponse(posterPath = null)

        // When
        val result = tvShowResponse.toEntity()

        // Then
        assertEquals("", result.posterPath)
    }

    @Test
    fun `toEntity with high rating returns correct rating value`() {
        // Given
        val movieResponse = createSampleMovieResponse(rating = 9.5)

        // When
        val result = movieResponse.toEntity()

        // Then
        assertEquals(9, result.rating)
        assertTrue(result.isMovie)
    }

    @Test
    fun `toEntity with decimal rating truncates correctly`() {
        // Given
        val movieResponse = createSampleMovieResponse(rating = 6.9)

        // When
        val result = movieResponse.toEntity()

        // Then
        assertEquals(6, result.rating)
        assertTrue(result.isMovie)
    }

    companion object {
        private fun createSampleMovieResponse(
            id: Int = 123,
            title: String = "Test Movie",
            posterPath: String? = "/test-poster.jpg",
            rating: Double = 8.5
        ) = RatedMovieResponse(
            id = id,
            adult = false,
            backdropPath = "/test-backdrop.jpg",
            title = title,
            posterPath = posterPath,
            voteAverage = rating,
            rating = rating
        )

        private fun createSampleTvShowResponse(
            id: Int = 456,
            name: String = "Test TV Show",
            posterPath: String? = "/test-poster.jpg",
            rating: Double = 7.5
        ) = RatedTvShowResponse(
            id = id,
            adult = false,
            backdropPath = "/test-backdrop.jpg",
            name = name,
            posterPath = posterPath,
            voteAverage = rating,
            rating = rating
        )
    }

} 