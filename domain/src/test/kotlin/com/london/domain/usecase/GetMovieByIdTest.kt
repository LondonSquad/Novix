package com.london.domain.usecase

import com.london.domain.GetMovieByIdFailedException
import com.london.domain.entity.moviedatails.CollectionDetails
import com.london.domain.entity.moviedatails.Genre
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.ProductionCompany
import com.london.domain.entity.moviedatails.ProductionCountry
import com.london.domain.entity.moviedatails.SpokenLanguage
import com.london.domain.repository.MovieDetailsRepository
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
    private lateinit var getMovieById: GetMovieById

    @Before
    fun setup() {
        movieRepository = mockk(relaxed = true)
        getMovieById = GetMovieById(movieRepository)
    }

    private fun fakeMovieDetailsDomain() = MovieDetails(
        adult = false,
        backdropUrl = "/inception_backdrop.jpg",
        belongsToCollection = CollectionDetails(1, "Inception Collection"),
        budget = 160_000_000,
        genres = listOf(
            Genre(1, "Sci-Fi"),
            Genre(2, "Thriller")
        ),
        homepage = "https://www.inceptionmovie.com",
        id = 123,
        imdbId = "tt1375666",
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalTitle = "Inception",
        overview = "A skilled thief is given a chance at redemption.",
        popularity = 98.5,
        posterUrl = "/inception_poster.jpg",
        productionCompanies = listOf(
            ProductionCompany(1, "/warner_logo.png", "Warner Bros.", "US")
        ),
        productionCountries = listOf(
            ProductionCountry("US", "United States")
        ),
        releaseDate = "2010-07-16",
        revenue = 829_895_144,
        runtime = 148,
        spokenLanguages = listOf(
            SpokenLanguage("English", "en", "English")
        ),
        status = "Released",
        tagline = "Your mind is the scene of the crime.",
        title = "Inception",
        video = false,
        voteAverage = "8.8",
        voteCount = 10000,
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
        assertEquals(2, result.genres.size)
        assertEquals("Sci-Fi", result.genres[0].name)

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
        val expectedMovie = fakeMovieDetailsDomain().copy(id = movieId, originalTitle = "Tenet")
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertEquals("Tenet", result.originalTitle)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `should handle movie with empty genres list`() = runTest {
        // Given
        val movieId = 789
        val movieWithNoGenres = fakeMovieDetailsDomain().copy(
            id = movieId, genres = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoGenres

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.genres.isEmpty())
        assertEquals(0, result.genres.size)
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
            id = movieId, genres = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoActors

        // When
        val result = getMovieById.invoke(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.genres.isEmpty())
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