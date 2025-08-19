package com.london.domain.usecase.rating

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.shared.MediaStates
import com.london.domain.entity.shared.MediaType
import com.london.domain.entity.shared.RatedMedia
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
    fun `getRatedMediaSorted returns empty list when no rated movies exist`() = runTest {
        // Given
        coEvery { movieRepository.getAllRatedMovies() } returns emptyList()

        // When
        val result = manageRatingUseCase.getRatedMediaSorted()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getRatedMediaSorted returns empty list when no rated tv shows exist`() = runTest {
        // Given
        coEvery { tvShowRepository.getAllRatedTvShows() } returns emptyList()

        // When
        val result = manageRatingUseCase.getRatedMediaSorted()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getRatedMediaSorted returns only movies sorted by rating when rated movies exist`() =
        runTest {
        // Given
        coEvery { movieRepository.getAllRatedMovies() } returns mockRatedMedia

        // When
            val result = manageRatingUseCase.getRatedMediaSorted()

        // Then
            assertEquals(mockRatedMedia.sortedByDescending { it.rating }, result)
    }

    @Test
    fun `getRatedMediaSorted returns only tv shows sorted by rating when rated tv shows exist`() =
        runTest {
        // Given
        coEvery { tvShowRepository.getAllRatedTvShows() } returns mockRatedMediaWithTvShows

        // When
            val result = manageRatingUseCase.getRatedMediaSorted()

        // Then
            assertEquals(mockRatedMediaWithTvShows.sortedByDescending { it.rating }, result)
    }

    @Test
    fun `addTvShowRatingById returns true when repository returns true`() = runTest {
        // Given
        coEvery { tvShowRepository.addTvShowById(TV_SHOW_ID, RATING) } returns true

        // When
        val result = manageRatingUseCase.addTvShowRatingById(TV_SHOW_ID, RATING)

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { tvShowRepository.addTvShowById(TV_SHOW_ID, RATING) }
    }

    @Test
    fun `addTvShowRatingById returns false when repository returns false`() = runTest {
        // Given
        coEvery { tvShowRepository.addTvShowById(TV_SHOW_ID, RATING) } returns false

        // When
        val result = manageRatingUseCase.addTvShowRatingById(TV_SHOW_ID, RATING)

        // Then        
        assertThat(result).isFalse()
        coVerify(exactly = 1) { tvShowRepository.addTvShowById(TV_SHOW_ID, RATING) }
    }

    @Test
    fun `add movie rating returns true when repository returns true`() = runTest {
        // Given
        coEvery { movieRepository.addMovieRatingById(MOVIE_ID, RATING) } returns true

        // When
        val result = manageRatingUseCase.addMovieRatingById(MOVIE_ID, RATING)

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { movieRepository.addMovieRatingById(MOVIE_ID, RATING) }
    }

    @Test
    fun `add movie rating returns false when repository returns false`() = runTest {
        // Given
        coEvery { movieRepository.addMovieRatingById(MOVIE_ID, RATING) } returns false

        // When 
        val result = manageRatingUseCase.addMovieRatingById(MOVIE_ID, RATING)

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 1) { movieRepository.addMovieRatingById(MOVIE_ID, RATING) }
    }

    @Test
    fun `add tv show episode rating returns true when repository returns true`() = runTest {
        // Given
        coEvery {
            tvShowRepository.addTvShowEpisode(
                tvShowId = TV_SHOW_ID,
                seasonNumber = SEASON_NUMBER,
                episodeNumber = EPISODE_NUMBER,
                rating = RATING
            )
        } returns true

        // When
        val result = manageRatingUseCase.addTvEpisodeRatingById(
            TV_SHOW_ID,
            RATING,
            SEASON_NUMBER,
            EPISODE_NUMBER
        )

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) {
            tvShowRepository.addTvShowEpisode(
                tvShowId = TV_SHOW_ID,
                rating = RATING,
                seasonNumber = SEASON_NUMBER,
                episodeNumber = EPISODE_NUMBER
            )
        }
    }

    @Test
    fun `add tv show episode rating returns false when repository returns false`() = runTest {
        // Given
        coEvery {
            tvShowRepository.addTvShowEpisode(
                TV_SHOW_ID,
                SEASON_NUMBER,
                EPISODE_NUMBER,
                RATING
            )
        } returns false

        // When
        val result = manageRatingUseCase.addTvEpisodeRatingById(
            TV_SHOW_ID,
            RATING,
            SEASON_NUMBER,
            EPISODE_NUMBER
        )

        assertThat(result).isFalse()
        coVerify(exactly = 1) {
            tvShowRepository.addTvShowEpisode(
                TV_SHOW_ID,
                SEASON_NUMBER,
                EPISODE_NUMBER,
                RATING
            )
        }
    }

    @Test
    fun `getAccountTvEpisode returns rating when repository returns rating`() = runTest {
        // Given
        coEvery {
            tvShowRepository.getAccountTvEpisode(
                tvShowId = TV_SHOW_ID,
                seasonNumber = SEASON_NUMBER,
                episodeNumber = EPISODE_NUMBER
            )
        } returns mockMovieStates()

        // When
        val result =
            manageRatingUseCase.getRateAccountTvEpisode(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

        // Then
        assertEquals(RATING, result)
    }

    @Test
    fun `getAccountTvShowState returns rating when repository returns rating`() = runTest {
        // Given
        coEvery {
            tvShowRepository.getAccountTvShowStateById(TV_SHOW_ID)
        } returns mockMovieStates()
        // When
        val result = manageRatingUseCase.getRateAccountTvShowStatesById(TV_SHOW_ID)

        // Then
        assertEquals(RATING, result)
    }

    @Test
    fun `getAccountMovieStatesById returns rating when repository returns rating`() = runTest {
        // Given
        coEvery { movieRepository.getAccountMovieStatesById(MOVIE_ID) } returns mockMovieStates()

        // When
        val result = manageRatingUseCase.getRateAccountMovieStatesById(MOVIE_ID)

        // Then
        assertEquals(RATING, result)
    }

    @Test
    fun `deleteMovieRating returns true when repository returns true`() = runTest {
        // Given
        coEvery { movieRepository.deleteMovieRating(MOVIE_ID) } returns true
        // When
        val result = manageRatingUseCase.deleteMovieRating(MOVIE_ID)
        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { movieRepository.deleteMovieRating(MOVIE_ID) }
    }

    @Test
    fun `deleteTvShowRating returns true when repository returns true`() = runTest {
        // Given
        coEvery { tvShowRepository.deleteTvShowRating(TV_SHOW_ID) } returns true
        // When
        val result = manageRatingUseCase.deleteTvShowRating(TV_SHOW_ID)
        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { tvShowRepository.deleteTvShowRating(TV_SHOW_ID) }
    }

    @Test
    fun `getRatedMediaById should return null when id is not found`() = runTest {
        // Given
        coEvery { movieRepository.getAllRatedMovies() } returns mockOnlyMovies
        coEvery { tvShowRepository.getAllRatedTvShows() } returns mockOnlyTvShows

        // When
        val result = manageRatingUseCase.getRatedMediaById(123)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `getRatedMediaById should return rated media when id is found`() = runTest {
        // Given
        coEvery { movieRepository.getAllRatedMovies() } returns mockOnlyMovies
        coEvery { tvShowRepository.getAllRatedTvShows() } returns mockOnlyTvShows

        // When
        val result = manageRatingUseCase.getRatedMediaById(1)

        // Then
        assertThat(result).isNotNull()
    }
    companion object {
        private const val MOVIE_ID = 456
        private const val TV_SHOW_ID = 456
        private const val SEASON_NUMBER = 1
        private const val EPISODE_NUMBER = 1
        private const val RATING = 8

        private fun mockMovieStates() = MediaStates(
            id = 123,
            rate = 8,
            favorite = false,
            watchlist = true
        )

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
