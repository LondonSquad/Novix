package com.london.domain.usecase.details.movie

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.error.GetCastByIdFailedException
import com.london.domain.error.GetMovieByIdFailedException
import com.london.domain.error.GetMovieCastFailedException
import com.london.domain.error.GetMovieImagesFailedException
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

class ManageMovieDetailsUseCaseTest {

    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var manageMovieDetailsUseCase: ManageMovieDetailsUseCase

    @Before
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        manageMovieDetailsUseCase = ManageMovieDetailsUseCase(movieRepository)
    }

    // region Movie Details Tests
    @Test
    fun `getMovieDetails should return movie details successfully`() = runTest {
        // Given
        val movieId = 123
        val expectedMovie = fakeMovieDetailsDomain()
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = manageMovieDetailsUseCase.getMovieDetails(movieId)

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
    fun `getMovieDetails should propagate Exception when repository fails`() = runTest {
        // Given
        val movieId = 123
        val expectedException = Exception("Failed to fetch movie details")
        coEvery { movieRepository.getMovieById(movieId) } throws expectedException

        // When & Then
        val exception = assertThrows<Exception> {
            manageMovieDetailsUseCase.getMovieDetails(movieId)
        }

        assertEquals("Failed to fetch movie details", exception.message)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should work with different movie IDs`() = runTest {
        // Given
        val movieId = 456
        val expectedMovie = fakeMovieDetailsDomain().copy(id = movieId, title = "Inception")
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = manageMovieDetailsUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertEquals("Inception", result.title)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should handle movie with empty genres list`() = runTest {
        // Given
        val movieId = 789
        val movieWithNoGenres = fakeMovieDetailsDomain().copy(
            id = movieId, genresId = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoGenres

        // When
        val result = manageMovieDetailsUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.genresId.isEmpty())
        assertEquals(0, result.genresId.size)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should handle movie with empty image list`() = runTest {
        // Given
        val movieId = 999
        val movieWithEmptyImages = fakeMovieDetailsDomain().copy(
            id = movieId, backdropUrl = ""
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithEmptyImages

        // When
        val result = manageMovieDetailsUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.backdropUrl.isEmpty())
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should handle movie with zero runtime`() = runTest {
        // Given
        val movieId = 111
        val movieWithZeroRuntime = fakeMovieDetailsDomain().copy(
            id = movieId,
            runtime = 0
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithZeroRuntime

        // When
        val result = manageMovieDetailsUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertEquals(0, result.runtime)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should handle RuntimeException and let it propagate`() = runTest {
        // Given
        val movieId = 555
        coEvery { movieRepository.getMovieById(movieId) } throws GetMovieByIdFailedException()

        // When & Then
        assertThrows<GetMovieByIdFailedException> {
            manageMovieDetailsUseCase.getMovieDetails(movieId)
        }
    }

    @Test
    fun `getMovieDetails should not call repository multiple times for same invocation`() = runTest {
        // Given
        val movieId = 666
        val expectedMovie = fakeMovieDetailsDomain().copy(id = movieId)
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        manageMovieDetailsUseCase.getMovieDetails(movieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }
    //endregion

    // region Similar Movies Tests
    @Test
    fun `getSimilarMovies should return similar movies when repository returns data`() = runTest {
        // given
        coEvery { movieRepository.getSimilarMoviesById(MOVIE_ID) } returns similarMovieMockList

        // when
        val result = manageMovieDetailsUseCase.getSimilarMovies(MOVIE_ID)

        // then
        assertThat(result).isEqualTo(similarMovieMockList)
        coVerify(exactly = 1) { movieRepository.getSimilarMoviesById(MOVIE_ID) }
    }

    @Test
    fun `getSimilarMovies should return empty list when repository returns empty list`() = runTest {
        // given
        coEvery { movieRepository.getSimilarMoviesById(MOVIE_ID) } returns emptyList()

        // when
        val result = manageMovieDetailsUseCase.getSimilarMovies(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { movieRepository.getSimilarMoviesById(MOVIE_ID) }
    }

    @Test
    fun `getSimilarMovies should throw GetMovieCastFailedException when repository throws domain exception`() =
        runTest {
            // given
            coEvery { movieRepository.getSimilarMoviesById(MOVIE_ID) } throws GetMovieCastFailedException()

            // when & then
            assertThrows<GetMovieCastFailedException> {
                manageMovieDetailsUseCase.getSimilarMovies(MOVIE_ID)
            }
            coVerify(exactly = 1) { movieRepository.getSimilarMoviesById(MOVIE_ID) }
        }

    @Test
    fun `getSimilarMovies should call repository with correct movie ID`() = runTest {
        // given
        val customId = 999
        coEvery { movieRepository.getSimilarMoviesById(customId) } returns emptyList()

        // when
        manageMovieDetailsUseCase.getSimilarMovies(customId)

        // then
        coVerify(exactly = 1) { movieRepository.getSimilarMoviesById(customId) }
    }

    @Test
    fun `getSimilarMovies should return different results for different movie IDs`() = runTest {
        // given
        val firstId = 111
        val secondId = 222

        val firstMovieList = listOf(createDummySimilarMovie(11, "Movie 11"))
        val secondMovieList = listOf(createDummySimilarMovie(20, "Movie 20"))

        coEvery { movieRepository.getSimilarMoviesById(firstId) } returns firstMovieList
        coEvery { movieRepository.getSimilarMoviesById(secondId) } returns secondMovieList

        // when
        val resultForFirstId = manageMovieDetailsUseCase.getSimilarMovies(firstId)
        val resultForSecondId = manageMovieDetailsUseCase.getSimilarMovies(secondId)

        // then
        assertThat(resultForFirstId).containsExactlyElementsIn(firstMovieList)
        assertThat(resultForSecondId).containsExactlyElementsIn(secondMovieList)
    }
    //endregion

    // region Movie Images Tests
    @Test
    fun `getFirstTenMovieImagesUseCase should return movie images when repository returns data`() = runTest {
        // given
        coEvery { movieRepository.getMovieImagesById(MOVIE_ID) } returns movieMockImages
        // when
        val result = manageMovieDetailsUseCase.getFirstTenMovieImagesUseCase(MOVIE_ID)
        // then
        assertThat(result).isEqualTo(movieMockImages)
    }

    @Test
    fun `getFirstTenMovieImagesUseCase should return empty list when repository returns empty list`() = runTest {
        // given
        coEvery { movieRepository.getMovieImagesById(MOVIE_ID) } returns emptyList()
        // when
        val result = manageMovieDetailsUseCase.getFirstTenMovieImagesUseCase(MOVIE_ID)
        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getFirstTenMovieImagesUseCase should throw exception when repository throws exception`() = runTest {
        // given
        coEvery { movieRepository.getMovieImagesById(MOVIE_ID) } throws GetMovieImagesFailedException()

        // when & then
        assertThrows<GetMovieImagesFailedException> {
            manageMovieDetailsUseCase.getFirstTenMovieImagesUseCase(MOVIE_ID)
        }
    }

    @Test
    fun `getFirstTenMovieImagesUseCase should limit the number of images returned when images over 10`() = runTest {
        // given
        val manyImages = (1..15).map { "/images/movie$it.jpg" }
        coEvery { movieRepository.getMovieImagesById(MOVIE_ID) } returns manyImages
        // when
        val result = manageMovieDetailsUseCase.getFirstTenMovieImagesUseCase(MOVIE_ID)
        // then
        assertThat(result).hasSize(10)
    }
    //endregion

    // region Movie Cast Tests
    @Test
    fun `getMovieCast should return cast when repository returns cast`() = runTest {
        // given
        coEvery { movieRepository.getMovieCastById(MOVIE_ID) } returns actorMockCast

        // when
        val result = manageMovieDetailsUseCase.getMovieCast(MOVIE_ID)

        // then
        assertThat(result).isEqualTo(actorMockCast)
    }

    @Test
    fun `getMovieCast should throw exception when repository throws exception`() = runTest {
        // given
        coEvery { movieRepository.getMovieCastById(MOVIE_ID) } throws GetCastByIdFailedException()

        // when & then
        assertThrows<GetCastByIdFailedException> {
            manageMovieDetailsUseCase.getMovieCast(MOVIE_ID)
        }
        coVerify(exactly = 1) { movieRepository.getMovieCastById(MOVIE_ID) }
    }

    @Test
    fun `getMovieCast should return empty list when repository returns empty list`() = runTest {
        // given
        coEvery { movieRepository.getMovieCastById(MOVIE_ID) } returns emptyList()

        // when
        val result = manageMovieDetailsUseCase.getMovieCast(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { movieRepository.getMovieCastById(MOVIE_ID) }
    }

    @Test
    fun `getMovieCast should call repository with correct movie ID`() = runTest {
        // given
        val customId = 999
        coEvery { movieRepository.getMovieCastById(customId) } returns emptyList()

        // when
        manageMovieDetailsUseCase.getMovieCast(customId)

        // then
        coVerify(exactly = 1) { movieRepository.getMovieCastById(customId) }
    }

    @Test
    fun `getMovieCast should return different results for different movie IDs`() = runTest {
        // given
        val actorCast = listOf(
            Actor(id = 3, name = "Tom Hardy", characterName = "Eames", profilePictureUrl = "/hardy.jpg")
        )

        coEvery { movieRepository.getMovieCastById(123) } returns actorMockCast
        coEvery { movieRepository.getMovieCastById(456) } returns actorCast

        // when
        val result1 = manageMovieDetailsUseCase.getMovieCast(123)
        val result2 = manageMovieDetailsUseCase.getMovieCast(456)

        // then
        assertThat(result1).hasSize(2)
        assertThat(result2).containsExactlyElementsIn(actorCast)
    }
    // endregion

    // region Movie Video Tests
    @Test
    fun `getMovieVideo should return movie videos when repository returns videos`() = runTest {
        // given
        coEvery { movieRepository.getMovieVideos(MOVIE_ID) } returns mockVideos

        // when
        val result = manageMovieDetailsUseCase.getMovieVideo(MOVIE_ID)

        // then
        assertThat(result[0]).isEqualTo(mockVideos[0])
        assertThat(result[1]).isEqualTo(mockVideos[1])
    }

    @Test
    fun `getMovieVideo should return empty list when repository returns no videos`() = runTest {
        // given
        coEvery { movieRepository.getMovieVideos(MOVIE_ID) } returns emptyList()

        // when
        val result = manageMovieDetailsUseCase.getMovieVideo(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieVideo should throw exception when repository throws exception`() = runTest {
        // given
        coEvery { movieRepository.getMovieVideos(MOVIE_ID) } throws RuntimeException("Network error")

        // when // then
        assertThrows<RuntimeException> {
            manageMovieDetailsUseCase.getMovieVideo(MOVIE_ID)
        }
    }
    //endregion

    private fun fakeMovieDetailsDomain() = MovieDetails(
        backdropUrl = "/inception_backdrop.jpg",
        genresId = listOf(1, 2, 3),
        id = 123,
        overview = "A skilled thief is given a chance at redemption.",
        posterUrl = "/inception_poster.jpg",
        releaseDate = "2010-07-16",
        runtime = 148,
        title = "Inception",
        video = false,
        voteAverage = "8.8",
    )

    private fun createDummySimilarMovie(id: Int, title: String) = Movie(
        id = id,
        name = title,
        posterUrl = "/backdrop_$id.jpg",
        genreIds = listOf(1, 2, 3),
        releaseYear = 2025,
        rating = 7,
    )

    private companion object {
        private const val MOVIE_ID = 123

        private val similarMovieMockList = listOf(
            createDummySimilarMovie(1, "Similar Movie 1"),
            createDummySimilarMovie(2, "Similar Movie 2")
        )

        private fun createDummySimilarMovie(id: Int, title: String) = Movie(
            id = id,
            name = title,
            posterUrl = "/backdrop_$id.jpg",
            genreIds = listOf(1, 2, 3),
            releaseYear = 2025,
            rating = 7,
        )

        val movieMockImages = listOf(
            "/images/movie1.jpg",
            "/images/movie2.jpg",
            "/images/movie3.jpg",
            "/images/movie4.jpg",
            "/images/movie5.jpg",
            "/images/movie6.jpg",
            "/images/movie8.jpg",
            "/images/movie9.jpg",
            "/images/movie10.jpg",
            "/images/movie11.jpg",
        )

        val actorMockCast = listOf(
            Actor(
                id = 1,
                name = "Leonardo DiCaprio",
                characterName = "Cobb",
                profilePictureUrl = "/leo.jpg"
            ),
            Actor(
                id = 2,
                name = "Joseph Gordon-Levitt",
                characterName = "Arthur",
                profilePictureUrl = "/jgl.jpg"
            )
        )

        val mockVideos = listOf(
            MovieVideo(
                id = "vid1",
                videoUrl = "https://youtube.com/watch?v=123",
                name = "Official Trailer",
                official = true,
                site = "YouTube",
            ), MovieVideo(
                id = "vid2",
                videoUrl = "https://youtube.com/watch?v=456",
                name = "Teaser",
                official = false,
                site = "YouTube",
            )
        )
    }
}