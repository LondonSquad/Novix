package com.london.data.repository

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.source.myrating.MyRatingRemoteDataSource
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
    private val remoteDataSource: MyRatingRemoteDataSource = mockk()
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
                    repository.addMovieRatingById(id = movieId, rating = 3)
                }
            }
        }

    @Test
    fun `addTvShowRating should return success result`() = runTest {
        coEvery { authPreferences.getSessionId() } returns USER_SESSION
        coEvery { authPreferences.getGuestSessionId() } returns GUEST_SESSION

        coEvery {
            remoteDataSource.addTvShowRating(
                tvShowId = MOVIE_ID,
                rating = RATING,
                guestSessionId = GUEST_SESSION,
                userSessionId = USER_SESSION
            )
        } returns Result.success(mockResponse)

        val result = repository.addTvShowById(
            id = MOVIE_ID,
            rating = RATING.toInt()
        )

        assertTrue(result)
    }

    @Test
    fun `addTvEpisode should return success result`() = runTest {
        val seasonNumber = 1
        val episodeNumber = 2

        coEvery { authPreferences.getSessionId() } returns USER_SESSION
        coEvery { authPreferences.getGuestSessionId() } returns GUEST_SESSION
        coEvery {
            remoteDataSource.addTvEpisode(
                tvShowId = MOVIE_ID,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                userSessionId = USER_SESSION,
                guestSessionId = GUEST_SESSION,
                rating = 8.0,
            )
        } returns Result.success(mockResponse)

        val result = repository.addTvEpisode(
            tvShowId = MOVIE_ID,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            rating = 8
        )

        assertTrue(result)
    }

    @Test
    fun `given user session when addTvShowById is called then return correct result`() =
        runTest {
            val tvShowId = 100
            val sessionId = USER_SESSION

            coEvery { authPreferences.getSessionId() } returns sessionId
            coEvery { authPreferences.getGuestSessionId() } returns null
            coEvery {
                remoteDataSource.addTvShowRating(
                    tvShowId = tvShowId,
                    userSessionId = sessionId,
                    guestSessionId = null,
                    rating = 7.0
                )
            } returns Result.success(fakeRatingResponse())

            val result = repository.addTvShowById(
                tvShowId,
                rating = 7
            )

            assertTrue(result)
        }

    @Test
    fun `given guest session when addTvShowById is called then return correct result`() =
        runTest {
            val tvShowId = 101
            val guestSessionId = GUEST_SESSION

            coEvery { authPreferences.getSessionId() } returns null
            coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
            coEvery {
                remoteDataSource.addTvShowRating(
                    tvShowId = tvShowId,
                    userSessionId = null,
                    guestSessionId = guestSessionId,
                    rating = 7.0,
                )
            } returns Result.success(fakeRatingResponse())

            val result = repository.addTvShowById(tvShowId, 7)

            assertTrue(result)
        }

    @Test
    fun `given no session when addTvShowById is called then return false`() =
        runTest {
            val tvShowId = 102

            coEvery { authPreferences.getSessionId() } returns null
            coEvery { authPreferences.getGuestSessionId() } returns null
            coEvery {
                remoteDataSource.addTvShowRating(
                    tvShowId = tvShowId,
                    userSessionId = null,
                    guestSessionId = null,
                    rating = 7.0
                )
            } returns Result.failure(RuntimeException("No session"))

            val result = repository.addTvShowById(tvShowId, 7)

            assertFalse(result)
        }

    companion object {
        private val mockResponse = RatingRemoteResponse(
            statusCode = 1,
            statusMessage = "Success"
        )

        private val MOVIE_ID = 123
        private val RATING = 7.0
        private val GUEST_SESSION = "mockGuestSessionId"
        private val USER_SESSION = "mockUserSessionId"
    }
}
