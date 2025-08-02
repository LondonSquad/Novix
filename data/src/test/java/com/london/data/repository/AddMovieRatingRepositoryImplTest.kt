package com.london.data.repository

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.details.rating.RatingResponse
import com.london.data.remote.source.details.movie.rating.AddMovieRatingRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class AddMovieRatingRepositoryImplTest {

    private lateinit var repository: AddMovieRatingRepositoryImpl
    private val remoteDataSource: AddMovieRatingRemoteDataSource = mockk()
    private val authPreferences: AuthPreferences = mockk()

    @Before
    fun setup() {
        repository = AddMovieRatingRepositoryImpl(remoteDataSource, authPreferences)
    }

    private fun fakeRatingResponse() = RatingResponse(
        statusCode = 1,
        statusMessage = "Success"
    )

    @Test
    fun `given user session when addMovieRatingById called then return true on success`() =
        runTest {
            // Given
            val movieId = 1
            val rating = 9.5
            val sessionId = "user-session-id"

            coEvery { authPreferences.getSessionId() } returns sessionId
            coEvery { authPreferences.getGuestSessionId() } returns null
            coEvery {
                remoteDataSource.addMovieRating(
                    movieId = movieId,
                    rating = rating,
                    userSessionId = sessionId,
                    guestSessionId = null,
                )
            } returns Result.success(fakeRatingResponse())

            // When
            val result = repository.addMovieRatingById(movieId, rating)

            // Then
            assertTrue(result)
        }

    @Test
    fun `given guest session when addMovieRatingById called then return true on success`() =
        runTest {
            // Given
            val movieId = 2
            val rating = 7.0
            val guestSessionId = "guest-session-id"

            coEvery { authPreferences.getSessionId() } returns null
            coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
            coEvery {
                remoteDataSource.addMovieRating(
                    movieId = movieId,
                    rating = rating,
                    guestSessionId = guestSessionId,
                    userSessionId = null
                )
            } returns Result.success(fakeRatingResponse())

            // When
            val result = repository.addMovieRatingById(movieId, rating)

            // Then
            assertTrue(result)
        }

    @Test
    fun `given no session when addMovieRatingById called then return false`() = runTest {

        // Given
        coEvery { authPreferences.getSessionId() } returns null
        coEvery { authPreferences.getGuestSessionId() } returns null

        // When
        val result = repository.addMovieRatingById(1, 5.0)

        // Then
        assertFalse(result)
    }
}
