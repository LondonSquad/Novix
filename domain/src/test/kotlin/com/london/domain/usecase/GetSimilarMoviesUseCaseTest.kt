package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.GetMovieCastFailedException
import com.london.domain.entity.moviedatails.SimilarMovie
import com.london.domain.repository.MovieDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetSimilarMoviesUseCaseTest {

    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var getSimilarMoviesUseCase: GetSimilarMoviesUseCase

    @Before
    fun setUp() {
        movieDetailsRepository = mockk()
        getSimilarMoviesUseCase = GetSimilarMoviesUseCase(movieDetailsRepository)
    }

    @Test
    fun `should return similar movies when repository returns data`() = runTest {
        // given
        coEvery { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) } returns similarMovieMockList

        // when
        val result = getSimilarMoviesUseCase.invoke(MOVIE_ID)

        // then
        assertThat(result).isEqualTo(similarMovieMockList)
        coVerify(exactly = 1) { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) }
    }

    @Test
    fun `should return empty list when repository returns empty list`() = runTest {
        // given
        coEvery { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) } returns emptyList()

        // when
        val result = getSimilarMoviesUseCase.invoke(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) }
    }

    @Test
    fun `should throw GetMovieCastFailedException when repository throws domain exception`() =
        runTest {
            // given
            coEvery { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) } throws GetMovieCastFailedException()

            // when & then
            assertThrows<GetMovieCastFailedException> {
                getSimilarMoviesUseCase.invoke(MOVIE_ID)
            }
            coVerify(exactly = 1) { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) }
        }

    @Test
    fun `should call repository with correct movie ID`() = runTest {
        // given
        val customId = 999
        coEvery { movieDetailsRepository.getSimilarMoviesById(customId) } returns emptyList()

        // when
        getSimilarMoviesUseCase.invoke(customId)

        // then
        coVerify(exactly = 1) { movieDetailsRepository.getSimilarMoviesById(customId) }
    }

    @Test
    fun `should return different results for different movie IDs`() = runTest {
        // given
        val firstId = 111
        val secondId = 222

        val firstMovieList = listOf(createDummySimilarMovie(11, "Movie 11"))
        val secondMovieList = listOf(createDummySimilarMovie(20, "Movie 20"))

        coEvery { movieDetailsRepository.getSimilarMoviesById(firstId) } returns firstMovieList
        coEvery { movieDetailsRepository.getSimilarMoviesById(secondId) } returns secondMovieList

        // when
        val resultForFirstId = getSimilarMoviesUseCase.invoke(firstId)
        val resultForSecondId = getSimilarMoviesUseCase.invoke(secondId)

        // then
        assertThat(resultForFirstId).containsExactlyElementsIn(firstMovieList)
        assertThat(resultForSecondId).containsExactlyElementsIn(secondMovieList)
    }

    private companion object {
        const val MOVIE_ID = 123

        val similarMovieMockList = listOf(
            createDummySimilarMovie(1, "Similar Movie 1"),
            createDummySimilarMovie(2, "Similar Movie 2")
        )

       private fun createDummySimilarMovie(id: Int, title: String) = SimilarMovie(
            id = id,
            adult = false,
            backdropPath = "/backdrop_$id.jpg",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "$title Original",
            overview = "Overview for $title",
            popularity = 123.45,
            posterPath = "/poster_$id.jpg",
            releaseDate = "2025-01-01",
            title = title,
            video = false,
            voteAverage = 7.5,
            voteCount = 200
        )
    }
}
