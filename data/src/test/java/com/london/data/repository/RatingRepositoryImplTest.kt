package com.london.data.repository

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.source.details.movie.rating.MovieRatingRemoteDataSource
import com.london.data.repository.rating.RatingRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse


@ExperimentalCoroutinesApi
class RatingRepositoryImplTest {

    private lateinit var repository: RatingRepositoryImpl
    private val remoteDataSource: MovieRatingRemoteDataSource = mockk()
    private val authPreferences: AuthPreferences = mockk()

    @Before
    fun setup() {
        repository = RatingRepositoryImpl(remoteDataSource, authPreferences)
    }

    private fun fakeRatingResponse() = RatingRemoteResponse(
        statusCode = 1,
        statusMessage = "Success",
    )

    @Test
    fun `given user session when addMovieRatingById called then return true on success`() =
        runTest {
            // Given
            val movieId = 1
            val rating = 9
            val sessionId = "user-session-id"

            coEvery { authPreferences.getSessionId() } returns sessionId
            coEvery { authPreferences.getGuestSessionId() } returns null
            coEvery {
                remoteDataSource.addMovieRating(
                    movieId = movieId,
                    rating = rating.toDouble(),
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
            val result = repository.addMovieRatingById(movieId, rating.toInt())

            // Then
            assertTrue(result)
        }

    @Test
    fun `given no session when addMovieRatingById called then return false`() = runTest {
        // Given
        val movieId = 1
        val rating = 5

        coEvery { authPreferences.getSessionId() } returns null
        coEvery { authPreferences.getGuestSessionId() } returns null

        coEvery {
            remoteDataSource.addMovieRating(
                movieId = movieId,
                rating = rating.toDouble(),
                userSessionId = null,
                guestSessionId = null
            )
        } returns Result.failure(RuntimeException("No session"))

        // When
        val result = repository.addMovieRatingById(movieId, rating)

        // Then
        assertFalse(result)
    }

    @Test
    fun `given failure from remote data source when getAccountMovieStatesById called then throw`() =
        runTest {
            // Given
            val movieId = 123
            val sessionId = "user-session"

            coEvery { authPreferences.getSessionId() } returns sessionId
            coEvery { authPreferences.getGuestSessionId() } returns null
            coEvery {
                remoteDataSource.addMovieRating(
                    movieId = movieId,
                    userSessionId = sessionId,
                    guestSessionId = null,
                    rating = 5.5
                )
            } returns Result.failure(RuntimeException("Network error"))

            // When && Then
            assertThrows(RuntimeException::class.java) {
                runTest {
                    repository.addMovieRatingById(
                        id = movieId, rating = 3
                    )
                }
            }
        }
}
