package com.london.domain.usecase.rating

import com.london.domain.entity.RatedMedia
import com.london.domain.entity.moviedatails.MediaStates
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.RatingRepository
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RatingUseCaseTest {
    private lateinit var repository: RatingRepository
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var ratingRepository: RatingRepository
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var ratingUseCase: RatingUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        tvShowRepository = mockk(relaxed = true)
        ratingRepository = mockk(relaxed = true)
        movieRepository = mockk(relaxed = true)
        ratingUseCase = RatingUseCase(
            repository = repository,
            tvShowRepository = tvShowRepository,
            ratingRepository = ratingRepository,
            movieRepository = movieRepository
        )
    }

    @Test
    fun `given valid id and rating when invoked then returns true`() = runTest {
        // Given
        val movieId = 456
        val rating = 7.5
        coEvery { repository.addMovieRatingById(movieId, rating.toInt()) } returns true

        // When
        val result = ratingUseCase.addMovieRatingById(movieId, rating.toInt())

        // Then
        Assert.assertTrue(result)
        coVerify(exactly = 1) { repository.addMovieRatingById(movieId, rating.toInt()) }
    }

    @Test
    fun `given valid id and rating when repository returns false then useCase returns false`() =
        runTest {
            // Given
            val movieId = 789
            val rating = 4.0
            coEvery { repository.addMovieRatingById(movieId, rating.toInt()) } returns false

            // When
            val result = ratingUseCase.addMovieRatingById(movieId, rating.toInt())

            // Then
            Assert.assertFalse(result)
            coVerify(exactly = 1) { repository.addMovieRatingById(movieId, rating.toInt()) }
        }

    @Test
    fun `given valid movie id when invoked then returns movie rate`() = runTest {
        // Given
        val movieId = 123
        val expectedRate = 8
        val movieStates = mockMovieStates()

        coEvery { movieRepository.getAccountMovieStatesById(movieId) } returns movieStates

        // When
        val result = ratingUseCase.getRateAccountMovieStatesById(movieId)

        // Then
        Assert.assertEquals(expectedRate, result)
    }

    @Test
    fun `getAllRated returns sorted list by rating`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMedia

        // When
        val result = ratingUseCase.getAllRated()

        // Then
        Assert.assertEquals(3, result.size)
        Assert.assertTrue(result[0].rating >= result[1].rating)
        Assert.assertTrue(result[1].rating >= result[2].rating)
    }

    @Test
    fun `getRatedMovies returns only movies sorted by rating`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMedia

        // When
        val result = ratingUseCase.getRatedMovies()

        // Then
        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result.all { it.isMovie })
        Assert.assertTrue(result[0].rating >= result[1].rating)
        Assert.assertEquals("Movie 2", result[0].title) // rating = 9
        Assert.assertEquals("Movie 1", result[1].title) // rating = 8
    }

    @Test
    fun `getRatedTvShows returns only tv shows sorted by rating`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMediaWithTvShows

        // When
        val result = ratingUseCase.getRatedTvShows()

        // Then
        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result.all { !it.isMovie })
        Assert.assertTrue(result[0].rating >= result[1].rating)
        Assert.assertEquals("TV Show 2", result[0].title) // rating = 9
        Assert.assertEquals("TV Show 1", result[1].title) // rating = 7
    }

    @Test
    fun `getAllRated returns empty list when repository returns empty`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns emptyList()

        // When
        val result = ratingUseCase.getAllRated()

        // Then
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `getRatedMovies returns empty list when no movies`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockOnlyTvShows

        // When
        val result = ratingUseCase.getRatedMovies()

        // Then
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `getRatedTvShows returns empty list when no tv shows`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockOnlyMovies

        // When
        val result = ratingUseCase.getRatedTvShows()

        // Then
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `getAllRated returns correct rating`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockSingleItem

        // When
        val result = ratingUseCase.getAllRated()

        // Then
        Assert.assertEquals(1, result.size)
        Assert.assertEquals(8, result[0].rating)
    }

    @Test
    fun `getAllRated sorts by rating descending`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockSortedItems

        // When
        val result = ratingUseCase.getAllRated()

        // Then
        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result[0].rating >= result[1].rating)
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
                isMovie = true
            ),
            RatedMedia(
                id = 2,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false
            ),
            RatedMedia(
                id = 3,
                title = "Movie 2",
                posterPath = "/movie2.jpg",
                rating = 9,
                isMovie = true
            )
        )

        private val mockRatedMediaWithTvShows = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true
            ),
            RatedMedia(
                id = 2,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false
            ),
            RatedMedia(
                id = 3,
                title = "TV Show 2",
                posterPath = "/tvshow2.jpg",
                rating = 9,
                isMovie = false
            )
        )

        private val mockOnlyTvShows = listOf(
            RatedMedia(
                id = 1,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false
            )
        )

        private val mockOnlyMovies = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true
            )
        )

        private val mockSingleItem = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true
            )
        )

        private val mockSortedItems = listOf(
            RatedMedia(
                id = 1,
                title = "First",
                posterPath = "/first.jpg",
                rating = 8,
                isMovie = true
            ),
            RatedMedia(
                id = 2,
                title = "Second",
                posterPath = "/second.jpg",
                rating = 7,
                isMovie = false
            )
        )
    }
}
