package com.london.domain.usecase

import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.error.GetMovieByIdFailedException
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.usecase.details.movie.GetMovieDetailsById
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

class GetMovieByIdTest {

    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var getMovieById: GetMovieDetailsById

    @Before
    fun setup() {
        movieRepository = mockk(relaxed = true)
        getMovieById = GetMovieDetailsById(movieRepository)
    }

    private fun fakeMovieDetailsDomain() = MovieDetails(
        backdropUrl = "/inception_backdrop.jpg",
        genresId = listOf(
          1,2,3
        ),
        id = 123,
        overview = "A skilled thief is given a chance at redemption.",
        posterUrl = "/inception_poster.jpg",
        releaseDate = "2010-07-16",
        runtime = 148,
        title = "Inception",
        video = false,
        voteAverage = "8.8",
    )


    @Test
    fun `invoke should return movie details successfully`() = runTest {
        // Given
        val movieId = 123
        val expectedMovie = fakeMovieDetailsDomain()
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(123, result.id)
        assertEquals("Inception", result.title)
        assertEquals("8.8", result.voteAverage)
        assertEquals(148, result.runtime)
        assertEquals("2010-07-16", result.releaseDate)
        assertEquals("A skilled thief is given a chance at redemption.", result.overview)
        assertEquals(3, result.genresId.size)

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
            getMovieById.invoke(movieId)
        }

        assertEquals("Failed to fetch movie details", exception.message)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should work with different movie IDs`() = runTest {
        // Given
        val movieId = 456
        val expectedMovie = fakeMovieDetailsDomain().copy(id = movieId, title = "Inception")
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertEquals("Inception", result.title)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with empty genres list`() = runTest {
        // Given
        val movieId = 789
        val movieWithNoGenres = fakeMovieDetailsDomain().copy(
            id = movieId, genresId = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoGenres

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.genresId.isEmpty())
        assertEquals(0, result.genresId.size)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with empty image list`() = runTest {
        // Given
        val movieId = 999
        val movieWithEmptyImages = fakeMovieDetailsDomain().copy(
            id = movieId, backdropUrl = ""
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithEmptyImages

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.backdropUrl.isEmpty())
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with zero runtime`() = runTest {
        // Given
        val movieId = 111
        val movieWithZeroRuntime = fakeMovieDetailsDomain().copy(
            id = movieId,
            runtime = 0
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithZeroRuntime

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertEquals(0, result.runtime)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }


    @Test
    fun `should handle movie with empty actors list`() = runTest {
        // Given
        val movieId = 222
        val movieWithNoActors = fakeMovieDetailsDomain().copy(
            id = movieId, genresId = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoActors

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.genresId.isEmpty())
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie without trailer`() = runTest {
        // Given
        val movieId = 444
        val movieWithoutTrailer = fakeMovieDetailsDomain().copy(
            id = movieId
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithoutTrailer

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle RuntimeException and let it propagate`() = runTest {
        // Given
        val movieId = 555
        coEvery { movieRepository.getMovieById(movieId) } throws GetMovieByIdFailedException()

        // When & Then
        assertThrows<GetMovieByIdFailedException> {
            getMovieById.invoke(movieId)
        }
    }

    @Test
    fun `should not call repository multiple times for same invocation`() = runTest {
        // Given
        val movieId = 666
        val expectedMovie = fakeMovieDetailsDomain().copy(id = movieId)
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        getMovieById.invoke(movieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }
}