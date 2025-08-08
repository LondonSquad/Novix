package com.london.data.repository.myrating

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.myrating.RatedMediaResponse
import com.london.data.remote.source.myrating.MyRatingRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MyRatingRepositoryImplTest {

    private lateinit var repository: MyRatingRepositoryImpl
    private lateinit var remoteDataSource: MyRatingRemoteDataSource
    private lateinit var authPreferences: AuthPreferences

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        authPreferences = mockk(relaxed = true)
        repository = MyRatingRepositoryImpl(remoteDataSource, authPreferences)
    }

    @Test
    fun `getAllRatedMedia returns combined movies and tv shows`() = runTest {
        // Given
        val accountId = 123
        val sessionId = "session123"

        val movieResponse = createSampleMovieResponse()
        val tvShowResponse = createSampleTvShowResponse()

        coEvery { authPreferences.getAccountId() } returns accountId
        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery {
            remoteDataSource.getAllRatedMovies(accountId, sessionId)
        } returns Result.success(createApiResponse(listOf(movieResponse)))
        coEvery {
            remoteDataSource.getAllRatedTvShows(accountId, sessionId)
        } returns Result.success(createApiResponse(listOf(tvShowResponse)))

        // When
        val result = repository.getAllRatedMedia()

        // Then
        assertEquals(2, result.size)

        val movie = result.find { it.isMovie }
        val tvShow = result.find { !it.isMovie }

        assertTrue(movie != null)
        assertTrue(tvShow != null)
        assertEquals("Test Movie", movie?.title)
        assertEquals("Test TV Show", tvShow?.title)
    }

    @Test
    fun `getAllRatedMedia handles empty responses`() = runTest {
        // Given
        val accountId = 123
        val sessionId = "session123"

        coEvery { authPreferences.getAccountId() } returns accountId
        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery {
            remoteDataSource.getAllRatedMovies(accountId, sessionId)
        } returns Result.success(createEmptyApiResponse())
        coEvery {
            remoteDataSource.getAllRatedTvShows(accountId, sessionId)
        } returns Result.success(createEmptyApiResponse())

        // When
        val result = repository.getAllRatedMedia()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getAllRatedMedia handles null session id`() = runTest {
        // Given
        val accountId = 123
        val sessionId = null

        coEvery { authPreferences.getAccountId() } returns accountId
        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery {
            remoteDataSource.getAllRatedMovies(accountId, "")
        } returns Result.success(createEmptyApiResponse())
        coEvery {
            remoteDataSource.getAllRatedTvShows(accountId, "")
        } returns Result.success(createEmptyApiResponse())

        // When
        val result = repository.getAllRatedMedia()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getAllRatedMedia maps movies correctly`() = runTest {
        // Given
        val accountId = 123
        val sessionId = "session123"

        val movieResponse = createSampleMovieResponse()

        coEvery { authPreferences.getAccountId() } returns accountId
        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery {
            remoteDataSource.getAllRatedMovies(accountId, sessionId)
        } returns Result.success(createApiResponse(listOf(movieResponse)))
        coEvery {
            remoteDataSource.getAllRatedTvShows(accountId, sessionId)
        } returns Result.success(createEmptyApiResponse())

        // When
        val result = repository.getAllRatedMedia()

        // Then
        assertEquals(1, result.size)
        val movie = result.first()
        assertEquals(1, movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals("https://image.tmdb.org/t/p/w500/movie-poster.jpg", movie.posterPath)
        assertEquals(8, movie.rating)
        assertTrue(movie.isMovie)
    }

    @Test
    fun `getAllRatedMedia maps tv shows correctly`() = runTest {
        // Given
        val accountId = 123
        val sessionId = "session123"

        val tvShowResponse = createSampleTvShowResponse()

        coEvery { authPreferences.getAccountId() } returns accountId
        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery {
            remoteDataSource.getAllRatedMovies(accountId, sessionId)
        } returns Result.success(createEmptyApiResponse())
        coEvery {
            remoteDataSource.getAllRatedTvShows(accountId, sessionId)
        } returns Result.success(createApiResponse(listOf(tvShowResponse)))

        // When
        val result = repository.getAllRatedMedia()

        // Then
        assertEquals(1, result.size)
        val tvShow = result.first()
        assertEquals(2, tvShow.id)
        assertEquals("Test TV Show", tvShow.title)
        assertEquals("https://image.tmdb.org/t/p/w500/tv-poster.jpg", tvShow.posterPath)
        assertEquals(7, tvShow.rating)
        assertTrue(!tvShow.isMovie)
    }

    @Test
    fun `getAllRatedMedia returns items in correct order`() = runTest {
        // Given
        val accountId = 123
        val sessionId = "session123"

        val movieResponse1 = createSampleMovieResponse(id = 1, rating = 8.5)
        val movieResponse2 = createSampleMovieResponse(id = 2, rating = 9.0)
        val tvShowResponse = createSampleTvShowResponse(id = 3, rating = 7.5)

        coEvery { authPreferences.getAccountId() } returns accountId
        coEvery { authPreferences.getSessionId() } returns sessionId
        coEvery {
            remoteDataSource.getAllRatedMovies(accountId, sessionId)
        } returns Result.success(createApiResponse(listOf(movieResponse1, movieResponse2)))
        coEvery {
            remoteDataSource.getAllRatedTvShows(accountId, sessionId)
        } returns Result.success(createApiResponse(listOf(tvShowResponse)))

        // When
        val result = repository.getAllRatedMedia()

        // Then
        assertEquals(3, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
        assertEquals(3, result[2].id)
    }

    companion object {
        private fun createSampleMovieResponse(
            id: Int = 1,
            title: String = "Test Movie",
            posterPath: String = "/movie-poster.jpg",
            rating: Double = 8.5
        ) = RatedMediaResponse(
            id = id,
            adult = false,
            backdropPath = "/movie-backdrop.jpg",
            title = title,
            posterPath = posterPath,
            voteAverage = rating,
            rating = rating
        )

        private fun createSampleTvShowResponse(
            id: Int = 2,
            title: String = "Test TV Show",
            posterPath: String = "/tv-poster.jpg",
            rating: Double = 7.5
        ) = RatedMediaResponse(
            id = id,
            adult = false,
            backdropPath = "/tv-backdrop.jpg",
            title = title,
            posterPath = posterPath,
            voteAverage = rating,
            rating = rating
        )

        private fun createApiResponse(items: List<RatedMediaResponse>, totalItems: Int = items.size) = ApiResponse(
            currentPage = 1,
            items = items,
            totalPages = if (totalItems > 0) 1 else 0,
            totalItems = totalItems
        )

        private fun createEmptyApiResponse() = ApiResponse<RatedMediaResponse>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 0,
            totalItems = 0
        )
    }

} 