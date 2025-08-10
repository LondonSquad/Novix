package com.london.data.repository.myrating

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.source.myrating.RatingRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RatingRepositoryImplTest {

    private lateinit var repository: RatingRepositoryImpl
    private lateinit var remoteDataSource: RatingRemoteDataSource
    private lateinit var authPreferences: AuthPreferences

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        authPreferences = mockk(relaxed = true)
        repository = RatingRepositoryImpl(remoteDataSource, authPreferences)
    }

    @Test
    fun `addMovieRatingById returns true on success`() = runTest {
        // Given
        val movieId = 123
        val rating = 8
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addMovieRating(
                movieId = movieId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addMovieRatingById(movieId, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `addMovieRatingById returns false on failure`() = runTest {
        // Given
        val movieId = 123
        val rating = 8
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addMovieRating(
                movieId = movieId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.failure(RuntimeException("Network error"))

        // When
        val result = repository.addMovieRatingById(movieId, rating)

        // Then
        assertFalse(result)
    }

    @Test
    fun `addTvShowById returns true on success`() = runTest {
        // Given
        val tvShowId = 456
        val rating = 7
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvShowRating(
                tvShowId = tvShowId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addTvShowById(tvShowId, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `addTvShowById returns false on failure`() = runTest {
        // Given
        val tvShowId = 456
        val rating = 7
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvShowRating(
                tvShowId = tvShowId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.failure(RuntimeException("Network error"))

        // When
        val result = repository.addTvShowById(tvShowId, rating)

        // Then
        assertFalse(result)
    }

    @Test
    fun `addTvEpisode returns true on success`() = runTest {
        // Given
        val tvShowId = 456
        val seasonNumber = 1
        val episodeNumber = 2
        val rating = 9
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvEpisode(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addTvEpisode(tvShowId, seasonNumber, episodeNumber, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `addTvEpisode returns false on failure`() = runTest {
        // Given
        val tvShowId = 456
        val seasonNumber = 1
        val episodeNumber = 2
        val rating = 9
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvEpisode(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.failure(RuntimeException("Network error"))

        // When
        val result = repository.addTvEpisode(tvShowId, seasonNumber, episodeNumber, rating)

        // Then
        assertFalse(result)
    }

    @Test
    fun `addMovieRatingById handles null session id`() = runTest {
        // Given
        val movieId = 123
        val rating = 8
        val sessionId = null
        val guestSessionId = "guest123"

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addMovieRating(
                movieId = movieId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addMovieRatingById(movieId, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `addTvShowById handles null guest session id`() = runTest {
        // Given
        val tvShowId = 456
        val rating = 7
        val sessionId = "session123"
        val guestSessionId = null

        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.addTvShowRating(
                tvShowId = tvShowId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addTvShowById(tvShowId, rating)

        // Then
        assertTrue(result)
    }

    companion object {
        private fun createRatingResponse() = RatingRemoteResponse(
            statusCode = 1,
            statusMessage = "Success"
        )
    }
} 