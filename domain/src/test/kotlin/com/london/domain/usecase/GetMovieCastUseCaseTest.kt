package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.error.GetCastByIdFailedException
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.usecase.details.movie.GetMovieCastUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMovieCastUseCaseTest {

    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var getMovieCastUseCase: GetMovieCastUseCase

    @Before
    fun setup() {
        movieRepository = mockk()
        getMovieCastUseCase = GetMovieCastUseCase(movieRepository)
    }

    @Test
    fun `should return cast when repository returns cast`() = runTest {
        // given
        coEvery { movieRepository.getMovieCastById(MOVIE_ID) } returns actorMockCast

        // when
        val result = getMovieCastUseCase.invoke(MOVIE_ID)

        // then
        assertThat(result).isEqualTo(actorMockCast)
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        // given
        coEvery { movieRepository.getMovieCastById(MOVIE_ID) } throws GetCastByIdFailedException()

        // when & then
        assertThrows<GetCastByIdFailedException> {
            getMovieCastUseCase.invoke(MOVIE_ID)
        }
        coVerify(exactly = 1) { movieRepository.getMovieCastById(MOVIE_ID) }
    }

    @Test
    fun `should return empty list when repository returns empty list`() = runTest {
        // given
        coEvery { movieRepository.getMovieCastById(MOVIE_ID) } returns emptyList()

        // when
        val result = getMovieCastUseCase.invoke(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { movieRepository.getMovieCastById(MOVIE_ID) }
    }


    @Test
    fun `should call repository with correct movie ID`() = runTest {
        // given
        val customId = 999
        coEvery { movieRepository.getMovieCastById(customId) } returns emptyList()

        // when
        getMovieCastUseCase.invoke(customId)

        // then
        coVerify(exactly = 1) { movieRepository.getMovieCastById(customId) }
    }

    @Test
    fun `should return different results for different movie IDs`() = runTest {
        // given
        val actorCast = listOf(
            Actor(id = 3, name = "Tom Hardy", characterName = "Eames", profilePictureUrl = "/hardy.jpg")
        )

        coEvery { movieRepository.getMovieCastById(123) } returns actorMockCast
        coEvery { movieRepository.getMovieCastById(456) } returns actorCast

        // when
        val result1 = getMovieCastUseCase.invoke(123)
        val result2 = getMovieCastUseCase.invoke(456)

        // then
        assertThat(result1).hasSize(2)
        assertThat(result2).containsExactlyElementsIn(actorCast)
    }

    private companion object {
        const val MOVIE_ID = 123

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
    }
}