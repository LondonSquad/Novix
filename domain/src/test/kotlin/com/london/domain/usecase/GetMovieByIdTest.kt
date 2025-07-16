package com.london.domain.usecase

import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.Genre
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie
import com.london.domain.repository.MovieDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class GetMovieByIdTest {

    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var getMovieById: GetMovieById

    @Before
    fun setup() {
        movieRepository = mockk(relaxed = true)
        getMovieById = GetMovieById(movieRepository)
    }

    private fun fakeMovieDetailsDomain() = MovieDetails(
        movieId = 123,
        movieImage = listOf("/inception.jpg", "/inception_backdrop.jpg"),
        movieName = "Inception",
        movieRating = "8.8",
        movieDuration = "148",
        releaseDate = "2010-07-16",
        movieOverview = "A skilled thief is given a chance at redemption",
        genres = listOf(
            Genre(1, "Sci-Fi"), Genre(2, "Thriller")
        ),
        actors = listOf(
            Actor(1, "Leonardo DiCaprio", "Cobb", "/leo.jpg"),
            Actor(2, "Joseph Gordon-Levitt", "Arthur", "/jgl.jpg")
        ),
        similarMovies = listOf(
            SimilarMovie("/interstellar.jpg", true , 1), SimilarMovie("/tenet.jpg", false , 1)
        ),
        movieHaveTrailer = true
    )

    @Test
    fun `invoke should return movie details successfully`() = runTest {
        // Given
        val movieId = 123
        val expectedMovie = fakeMovieDetailsDomain()
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = getMovieById(movieId)

        // Then
        assertNotNull(result)
        assertEquals(123, result.movieId)
        assertEquals("Inception", result.movieName)
        assertEquals("8.8", result.movieRating)
        assertEquals("148", result.movieDuration)
        assertEquals("2010-07-16", result.releaseDate)
        assertEquals("A skilled thief is given a chance at redemption", result.movieOverview)
        assertEquals(2, result.genres.size)
        assertEquals("Sci-Fi", result.genres[0].name)
        assertEquals("Thriller", result.genres[1].name)
        assertEquals(2, result.actors.size)
        assertEquals("Leonardo DiCaprio", result.actors[0].name)
        assertEquals(2, result.similarMovies.size)
        assertEquals(2, result.movieImage.size)
        assertTrue(result.movieHaveTrailer)

        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should propagate Exception when repository fails`() = runTest {
        // Given
        val movieId = 123
        val expectedException = Exception("Failed to fetch movie details")
        coEvery { movieRepository.getMovieById(movieId) } throws expectedException

        // When & Then
        val exception = assertThrows<Exception> {
            getMovieById(movieId)
        }

        assertEquals("Failed to fetch movie details", exception.message)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should work with different movie IDs`() = runTest {
        // Given
        val movieId = 456
        val expectedMovie = fakeMovieDetailsDomain().copy(movieId = movieId, movieName = "Tenet")
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = getMovieById(movieId)

        // Then
        assertEquals(456, result.movieId)
        assertEquals("Tenet", result.movieName)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with empty genres list`() = runTest {
        // Given
        val movieId = 789
        val movieWithNoGenres = fakeMovieDetailsDomain().copy(
            movieId = movieId, genres = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoGenres

        // When
        val result = getMovieById(movieId)

        // Then
        assertEquals(movieId, result.movieId)
        assertTrue(result.genres.isEmpty())
        assertEquals(0, result.genres.size)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with empty image list`() = runTest {
        // Given
        val movieId = 999
        val movieWithEmptyImages = fakeMovieDetailsDomain().copy(
            movieId = movieId, movieImage = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithEmptyImages

        // When
        val result = getMovieById(movieId)

        // Then
        assertEquals(movieId, result.movieId)
        assertTrue(result.movieImage.isEmpty())
        assertEquals(0, result.movieImage.size)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with zero duration`() = runTest {
        // Given
        val movieId = 111
        val movieWithZeroDuration = fakeMovieDetailsDomain().copy(
            movieId = movieId, movieDuration = "0"
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithZeroDuration

        // When
        val result = getMovieById(movieId)

        // Then
        assertEquals(movieId, result.movieId)
        assertEquals("0", result.movieDuration)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with empty actors list`() = runTest {
        // Given
        val movieId = 222
        val movieWithNoActors = fakeMovieDetailsDomain().copy(
            movieId = movieId, actors = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoActors

        // When
        val result = getMovieById(movieId)

        // Then
        assertEquals(movieId, result.movieId)
        assertTrue(result.actors.isEmpty())
        assertEquals(0, result.actors.size)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with empty similar movies list`() = runTest {
        // Given
        val movieId = 333
        val movieWithNoSimilar = fakeMovieDetailsDomain().copy(
            movieId = movieId, similarMovies = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoSimilar

        // When
        val result = getMovieById(movieId)

        // Then
        assertEquals(movieId, result.movieId)
        assertTrue(result.similarMovies.isEmpty())
        assertEquals(0, result.similarMovies.size)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie without trailer`() = runTest {
        // Given
        val movieId = 444
        val movieWithoutTrailer = fakeMovieDetailsDomain().copy(
            movieId = movieId, movieHaveTrailer = false
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithoutTrailer

        // When
        val result = getMovieById(movieId)

        // Then
        assertEquals(movieId, result.movieId)
        assertFalse(result.movieHaveTrailer)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle RuntimeException and let it propagate`() = runTest {
        // Given
        val movieId = 555
        val runtimeException = RuntimeException("Network error")
        coEvery { movieRepository.getMovieById(movieId) } throws runtimeException

        // When & Then
        val exception = assertThrows<RuntimeException> {
            getMovieById(movieId)
        }

        assertEquals("Network error", exception.message)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should not call repository multiple times for same invocation`() = runTest {
        // Given
        val movieId = 666
        val expectedMovie = fakeMovieDetailsDomain().copy(movieId = movieId)
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        getMovieById(movieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }
}