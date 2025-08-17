package com.london.domain.usecase.rating

import com.london.domain.entity.MediaStates
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ManageRatingUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var manageRatingUseCase: ManageRatingUseCase

    @Before
    fun setup() {
        movieRepository = mockk(relaxed = true)
        tvShowRepository = mockk(relaxed = true)
        manageRatingUseCase = ManageRatingUseCase(
            tvShowRepository = tvShowRepository,
            movieRepository = movieRepository
        )
    }

    @Test
    fun `given valid id and rating when invoked then returns true`() = runTest {
        // Given
        val movieId = 456
        val rating = 7.5
        coEvery { movieRepository.addMovieRatingById(movieId, rating.toInt()) } returns true

        // When
        val result = manageRatingUseCase.addMovieRatingById(movieId, rating.toInt())

        // Then
        Assert.assertTrue(result)
        coVerify(exactly = 1) { movieRepository.addMovieRatingById(movieId, rating.toInt()) }
    }

    @Test
    fun `given valid id and rating when repository returns false then useCase returns false`() =
        runTest {
            // Given
            val movieId = 789
            val rating = 4.0
            coEvery { movieRepository.addMovieRatingById(movieId, rating.toInt()) } returns false

            // When
            val result = manageRatingUseCase.addMovieRatingById(movieId, rating.toInt())

            // Then
            Assert.assertFalse(result)
            coVerify(exactly = 1) { movieRepository.addMovieRatingById(movieId, rating.toInt()) }
        }

    @Test
    fun `given valid movie id when invoked then returns movie rate`() = runTest {
        // Given
        val movieId = 123
        val expectedRate = 8
        val movieStates = mockMovieStates()

        coEvery { movieRepository.getAccountMovieStatesById(movieId) } returns movieStates

        // When
        val result = manageRatingUseCase.getRateAccountMovieStatesById(movieId)

        // Then
        assertEquals(expectedRate, result)
    }

    @Test
    fun `getRatedMovies returns only movies sorted by rating`() = runTest {
        // Given
        coEvery { movieRepository.getAllRatedMovies() } returns mockRatedMedia

        // When
        val result = manageRatingUseCase.getAllRatedMovies()

        // Then
        assertEquals(2, result.size)
        Assert.assertTrue(result[1].rating >= result[0].rating)
        assertEquals("Movie 2", result[1].title) // rating = 8
        assertEquals("Movie 1", result[0].title) // rating = 9
    }

    @Test
    fun `getRatedTvShows returns only tv shows sorted by rating`() = runTest {
        // Given
        coEvery { tvShowRepository.getAllRatedTvShows() } returns mockRatedMediaWithTvShows

        // When
        val result = manageRatingUseCase.getAllRatedTvShows()

        // Then
        assertEquals(2, result.size)
        Assert.assertTrue(result[1].rating >= result[0].rating)
        Assert.assertTrue(result[1].rating >= result[0].rating)
        assertEquals("TV Show 2", result[1].title)
        assertEquals("TV Show 1", result[0].title)
    }


    @Test
    fun `getRatedMovies returns empty list when no movies`() = runTest {
        // Given
        coEvery { movieRepository.getAllRatedMovies() } returns emptyList()

        // When
        val result = manageRatingUseCase.getAllRatedMovies()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getRatedTvShows returns empty list when no tv shows`() = runTest {
        // Given
        coEvery { tvShowRepository.getAllRatedTvShows() } returns emptyList()

        // When
        val result = manageRatingUseCase.getAllRatedTvShows()

        // Then
        assertEquals(0, result.size)
    }

    private fun mockMovieStates() = MediaStates(
        id = 123,
        rate = 8,
        favorite = false,
        watchlist = true
    )

    companion object {
        private val mockRatedMedia = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                mediaType = MediaType.Movie
            ),
            RatedMedia(
                id = 3,
                title = "Movie 2",
                posterPath = "/movie2.jpg",
                rating = 9,
                mediaType = MediaType.Movie
            )
        )

        private val mockRatedMediaWithTvShows = listOf(
            RatedMedia(
                id = 2,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                mediaType = MediaType.TvShow
            ),
            RatedMedia(
                id = 3,
                title = "TV Show 2",
                posterPath = "/tvshow2.jpg",
                rating = 9,
                mediaType = MediaType.TvShow
            )
        )

        private val mockOnlyTvShows = listOf(
            RatedMedia(
                id = 1,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                mediaType = MediaType.TvShow
            )
        )

        private val mockOnlyMovies = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                mediaType = MediaType.Movie
            )
        )
    }
}
