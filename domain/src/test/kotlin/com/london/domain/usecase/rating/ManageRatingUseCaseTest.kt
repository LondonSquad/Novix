package com.london.domain.usecase.rating

import com.london.domain.entity.RatedMedia
import com.london.domain.entity.moviedatails.MediaStates
import com.london.domain.entity.recent.MediaType
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
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
    fun `addMovieRatingById should return true when repository returns true`() = runTest {
        // Given
        val movieId = 456
        val rating = 7.5
        coEvery { movieRepository.addMovieRatingById(movieId, rating.toInt()) } returns true

        // When
        val result = manageRatingUseCase.addMovieRatingById(movieId, rating.toInt())

        // Then
        Assert.assertTrue(result)
    }

    @Test
    fun `deleteMovieRating should return true when repository returns true`() = runTest {
        val movieId = 10
        coEvery { movieRepository.deleteMovieRating(movieId) } returns true

        val result = manageRatingUseCase.deleteMovieRating(movieId)

        Assert.assertTrue(result)
    }

    @Test
    fun `deleteTvShowRating should return true when repository returns true`() = runTest {
        val tvId = 11
        coEvery { tvShowRepository.deleteTvShowRating(tvId) } returns true

        val result = manageRatingUseCase.deleteTvShowRating(tvId)

        Assert.assertTrue(result)
    }

    @Test
    fun `addTvShowRatingById should return true when repository returns true`() = runTest {
        val tvId = 12
        val rating = 9
        coEvery { tvShowRepository.addTvShowById(tvId, rating) } returns true

        val result = manageRatingUseCase.addTvShowRatingById(tvId, rating)

        Assert.assertTrue(result)
    }

    @Test
    fun `addTvEpisodeRatingById should return true when repository returns true`() = runTest {
        val tvId = 1
        val season = 2
        val episode = 3
        val rating = 8
        coEvery { tvShowRepository.addTvShowEpisode(tvId, season, episode, rating) } returns true

        val result = manageRatingUseCase.addTvEpisodeRatingById(tvId, rating, season, episode)

        Assert.assertTrue(result)
    }

    @Test
    fun `getRateAccountTvEpisode should return rate when repository returns states`() = runTest {
        val tvId = 5
        val season = 1
        val episode = 7
        val expectedRate = 6
        coEvery { tvShowRepository.getAccountTvEpisode(tvId, season, episode) } returns MediaStates(
            id = 0, rate = expectedRate, favorite = false, watchlist = false
        )

        val result = manageRatingUseCase.getRateAccountTvEpisode(tvId, season, episode)

        assertEquals(expectedRate, result)
    }

    @Test
    fun `addMovieRatingById should return false when repository returns false`() =
        runTest {
            // Given
            val movieId = 789
            val rating = 4.0
            coEvery { movieRepository.addMovieRatingById(movieId, rating.toInt()) } returns false

            // When
            val result = manageRatingUseCase.addMovieRatingById(movieId, rating.toInt())

            // Then
            Assert.assertFalse(result)
        }

    @Test
    fun `getRateAccountMovieStatesById should return movie rate when repository returns states`() = runTest {
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
    fun `getRateAccountTvShowStatesById should return tv show rate when repository returns states`() = runTest {
        val tvShowId = 321
        val expectedRate = 7
        coEvery { tvShowRepository.getAccountTvShowStateById(tvShowId) } returns MediaStates(
            id = tvShowId,
            rate = expectedRate,
            favorite = false,
            watchlist = false
        )

        val result = manageRatingUseCase.getRateAccountTvShowStatesById(tvShowId)

        assertEquals(expectedRate, result)
    }

    @Test
    fun `getRatedMovies should return only movies sorted by rating when repository returns movies`() = runTest {
        // Given
        coEvery { movieRepository.getAllRatedMovies() } returns mockRatedMedia
        coEvery { tvShowRepository.getAllRatedTvShows() } returns emptyList()

        // When
        val result = manageRatingUseCase
            .getRatedMediaSorted()
            .filter { it.mediaType == MediaType.Movie }

        // Then
        assertEquals(listOf("Movie 2", "Movie 1"), result.map { it.title })
    }

    @Test
    fun `getRatedTvShows should return only tv shows sorted by rating when repository returns tv shows`() = runTest {
        // Given
        coEvery { tvShowRepository.getAllRatedTvShows() } returns mockRatedMediaWithTvShows
        coEvery { movieRepository.getAllRatedMovies() } returns emptyList()

        // When
        val result = manageRatingUseCase
            .getRatedMediaSorted()
            .filter { it.mediaType == MediaType.TvShow }

        // Then
        assertEquals(listOf("TV Show 2", "TV Show 1"), result.map { it.title })
    }


    @Test
    fun `getRatedMovies should return empty list when repository returns empty`() = runTest {
        // Given
        coEvery { movieRepository.getAllRatedMovies() } returns emptyList()
        coEvery { tvShowRepository.getAllRatedTvShows() } returns mockOnlyTvShows

        // When
        val result = manageRatingUseCase
            .getRatedMediaSorted()
            .filter { it.mediaType == MediaType.Movie }

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getRatedTvShows should return empty list when repository returns empty`() = runTest {
        // Given
        coEvery { tvShowRepository.getAllRatedTvShows() } returns emptyList()
        coEvery { movieRepository.getAllRatedMovies() } returns mockOnlyMovies

        // When
        val result = manageRatingUseCase
            .getRatedMediaSorted()
            .filter { it.mediaType == MediaType.TvShow }

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
