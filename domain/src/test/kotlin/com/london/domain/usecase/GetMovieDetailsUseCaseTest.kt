package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.GetCastByIdFailedException
import com.london.domain.GetMovieDetailsFailedException
import com.london.domain.GetMovieImagesFailedException
import com.london.domain.GetSimilarMoviesFailedException
import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.Genre
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMovieDetailsUseCaseTest {

    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var getMovieById: GetMovieById
    private lateinit var getMovieImagesUseCase: GetMovieImagesUseCase
    private lateinit var getMovieCastUseCase: GetMovieCastUseCase
    private lateinit var getSimilarMoviesUseCase: GetSimilarMoviesUseCase

    @Before
    fun setUp() {
        getMovieById = mockk()
        getMovieImagesUseCase = mockk()
        getMovieCastUseCase = mockk()
        getSimilarMoviesUseCase = mockk()

        getMovieDetailsUseCase = GetMovieDetailsUseCase(
            getMovieById, getMovieImagesUseCase, getMovieCastUseCase, getSimilarMoviesUseCase
        )
    }

    @Test
    fun `should combine all movie details correctly`() = runTest {
        // given
        coEvery { getMovieById.invoke(MOVIE_ID) } returns baseMovie
        coEvery { getMovieImagesUseCase.invoke(MOVIE_ID) } returns fakeImages
        coEvery { getMovieCastUseCase.invoke(MOVIE_ID) } returns fakeCast
        coEvery { getSimilarMoviesUseCase.invoke(MOVIE_ID) } returns fakeSimilarMovies

        // when
        val result = getMovieDetailsUseCase(MOVIE_ID)

        // then
        assertThat(result).isEqualTo(
            MovieDetails(
                movieId = baseMovie.movieId,
                movieImage = fakeImages,
                movieName = baseMovie.movieName,
                movieRating = baseMovie.movieRating,
                movieDuration = baseMovie.movieDuration,
                releaseDate = baseMovie.releaseDate,
                movieOverview = baseMovie.movieOverview,
                genres = baseMovie.genres,
                actors = fakeCast,
                similarMovies = fakeSimilarMovies,
                movieHaveTrailer = baseMovie.movieHaveTrailer
            )
        )
    }

    @Test
    fun `should throw exception when getMovieById fails`() = runTest {
        //Given
        coEvery { getMovieById.invoke(MOVIE_ID) } throws GetMovieDetailsFailedException()

        //When&Then
        assertThrows<GetMovieDetailsFailedException> {
            getMovieDetailsUseCase(MOVIE_ID)
        }
    }

    @Test
    fun `should throw GetMovieImagesFailedException when getMovieImagesUseCase fails`() = runTest {
        //Given
        coEvery { getMovieById.invoke(MOVIE_ID) } returns baseMovie
        coEvery { getMovieImagesUseCase.invoke(MOVIE_ID) } throws GetMovieImagesFailedException()

        //When&Then
        assertThrows<GetMovieImagesFailedException> {
            getMovieDetailsUseCase(MOVIE_ID)
        }
    }

    @Test
    fun `should throw exception when getMovieCastUseCase fails`() = runTest {
        //Given
        coEvery { getMovieById.invoke(MOVIE_ID) } returns baseMovie
        coEvery { getMovieImagesUseCase.invoke(MOVIE_ID) } returns fakeImages
        coEvery { getMovieCastUseCase.invoke(MOVIE_ID) } throws GetCastByIdFailedException()

        assertThrows<GetCastByIdFailedException> {
            getMovieDetailsUseCase(MOVIE_ID)
        }
    }

    @Test
    fun `should throw exception when getSimilarMoviesUseCase fails`() = runTest {
        coEvery { getMovieById.invoke(MOVIE_ID) } returns baseMovie
        coEvery { getMovieImagesUseCase.invoke(MOVIE_ID) } returns fakeImages
        coEvery { getMovieCastUseCase.invoke(MOVIE_ID) } returns fakeCast
        coEvery { getSimilarMoviesUseCase.invoke(MOVIE_ID) } throws GetSimilarMoviesFailedException()

        assertThrows<GetSimilarMoviesFailedException> {
            getMovieDetailsUseCase(MOVIE_ID)
        }
    }

    private companion object {
        const val MOVIE_ID = 123

        val baseMovie = MovieDetails(
            movieId = MOVIE_ID,
            movieImage = emptyList(),
            movieName = "Inception",
            movieRating = "8.8",
            movieDuration = "148 min",
            releaseDate = "2010-07-16",
            movieOverview = "A mind-bending thriller.",
            genres = listOf(
                Genre(id = 1, name = "Action"), Genre(id = 2, name = "Sci-Fi")
            ),
            actors = emptyList(),
            similarMovies = emptyList(),
            movieHaveTrailer = true
        )

        val fakeImages = listOf(
            "/images/movie1.jpg", "/images/movie2.jpg"
        )

        val fakeCast = listOf(
            Actor(
                id = 1,
                name = "Leonardo DiCaprio",
                characterName = "Cobb",
                profilePicture = "/leo.jpg"
            ), Actor(
                id = 2,
                name = "Joseph Gordon-Levitt",
                characterName = "Arthur",
                profilePicture = "/jgl.jpg"
            )
        )

        val fakeSimilarMovies = listOf(
            SimilarMovie(
                id = 999, image = "/similar/interstellar.jpg", isSaved = true
            )
        )
    }
}