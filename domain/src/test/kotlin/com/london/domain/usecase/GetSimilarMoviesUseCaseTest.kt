package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.GetMovieCastFailedException
import com.london.domain.GetSimilarMoviesFailedException
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
        val result = getSimilarMoviesUseCase(MOVIE_ID)

        // then
        assertThat(result).isEqualTo(similarMovieMockList)
        coVerify(exactly = 1) { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) }
    }

    @Test
    fun `should return empty list when repository returns empty list`() = runTest {
        // given
        coEvery { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) } returns emptyList()

        // when
        val result = getSimilarMoviesUseCase(MOVIE_ID)

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
                getSimilarMoviesUseCase(MOVIE_ID)
            }
            coVerify(exactly = 1) { movieDetailsRepository.getSimilarMoviesById(MOVIE_ID) }
        }


    @Test
    fun `should call repository with correct movie ID`() = runTest {
        // given
        val customId = 999
        coEvery { movieDetailsRepository.getSimilarMoviesById(customId) } returns emptyList()

        // when
        getSimilarMoviesUseCase(customId)

        // then
        coVerify(exactly = 1) { movieDetailsRepository.getSimilarMoviesById(customId) }
    }

    @Test
    fun `should return different results for different movie IDs`() = runTest {
        // given
        val firstId = 111
        val secondId = 222

        val firstMovieList = listOf(
            SimilarMovie(id = 11, image = "/movie_11.jpg", isSaved = true)
        )
        val secondMovieList = listOf(
            SimilarMovie(id = 20, image = "/movie_20.jpg", isSaved = false)
        )

        coEvery { movieDetailsRepository.getSimilarMoviesById(firstId) } returns firstMovieList
        coEvery { movieDetailsRepository.getSimilarMoviesById(secondId) } returns secondMovieList

        // when
        val resultForFirstId = getSimilarMoviesUseCase(firstId)
        val resultForSecondId = getSimilarMoviesUseCase(secondId)

        // then
        assertThat(resultForFirstId).containsExactlyElementsIn(firstMovieList)
        assertThat(resultForSecondId).containsExactlyElementsIn(secondMovieList)
    }


    private companion object {
        const val MOVIE_ID = 123

        val similarMovieMockList = listOf(
            SimilarMovie(
                id = 1, image = "/similar_movie_1.jpg", isSaved = false
            ), SimilarMovie(
                id = 2, image = "/similar_movie_2.jpg", isSaved = true
            )
        )
    }
}
