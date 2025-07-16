package com.london.domain.usecase

import com.london.domain.entity.Actor
import com.london.domain.repository.MovieDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class GetMovieCastUseCaseTest {

    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var getMovieCastUseCase: GetMovieCastUseCase

    @Before
    fun setup() {
        movieRepository = mockk(relaxed = true)
        getMovieCastUseCase = GetMovieCastUseCase(movieRepository)
    }

    private fun fakeCast() = listOf(
        Actor(
            id = 1, name = "Leonardo DiCaprio", characterName = "Cobb", profilePicture = "/leo.jpg"
        ), Actor(
            id = 2,
            name = "Joseph Gordon-Levitt",
            characterName = "Arthur",
            profilePicture = "/jgl.jpg"
        )
    )

    @Test
    fun `should return cast successfully`() = runTest {
        // Given
        val movieId = 123
        val expectedCast = fakeCast()
        coEvery { movieRepository.getMovieCastById(movieId) } returns expectedCast

        // When
        val result = getMovieCastUseCase(movieId)

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Leonardo DiCaprio", result[0].name)
        assertEquals("Cobb", result[0].characterName)
        assertEquals("/leo.jpg", result[0].profilePicture)
        assertEquals("Joseph Gordon-Levitt", result[1].name)

        coVerify(exactly = 1) { movieRepository.getMovieCastById(movieId) }
    }

    @Test
    fun `should return empty list when repository returns no cast`() = runTest {
        // Given
        val movieId = 456
        coEvery { movieRepository.getMovieCastById(movieId) } returns emptyList()

        // When
        val result = getMovieCastUseCase(movieId)

        // Then
        assertTrue(result.isEmpty())
        assertEquals(0, result.size)
        coVerify(exactly = 1) { movieRepository.getMovieCastById(movieId) }
    }

    @Test
    fun `should propagate Exception when repository fails`() = runTest {
        // Given
        val movieId = 789
        val expectedException = Exception("Failed to fetch cast")
        coEvery { movieRepository.getMovieCastById(movieId) } throws expectedException

        // When & Then
        val exception = assertThrows<Exception> {
            getMovieCastUseCase(movieId)
        }
        assertEquals("Failed to fetch cast", exception.message)
        coVerify(exactly = 1) { movieRepository.getMovieCastById(movieId) }
    }

    @Test
    fun `should handle RuntimeException and propagate`() = runTest {
        // Given
        val movieId = 999
        val runtimeException = RuntimeException("Network error")
        coEvery { movieRepository.getMovieCastById(movieId) } throws runtimeException

        // When & Then
        val exception = assertThrows<RuntimeException> {
            getMovieCastUseCase(movieId)
        }
        assertEquals("Network error", exception.message)
        coVerify(exactly = 1) { movieRepository.getMovieCastById(movieId) }
    }

    @Test
    fun `should work with different movie IDs`() = runTest {
        // Given
        val movieId = 111
        val expectedCast = listOf(
            Actor(
                id = 3, name = "Tom Hardy", characterName = "Eames", profilePicture = "/hardy.jpg"
            )
        )
        coEvery { movieRepository.getMovieCastById(movieId) } returns expectedCast

        // When
        val result = getMovieCastUseCase(movieId)

        // Then
        assertEquals(1, result.size)
        assertEquals("Tom Hardy", result[0].name)
        assertEquals("Eames", result[0].characterName)
        coVerify(exactly = 1) { movieRepository.getMovieCastById(movieId) }
    }

    @Test
    fun `should not call repository multiple times for same invocation`() = runTest {
        // Given
        val movieId = 222
        val expectedCast = fakeCast()
        coEvery { movieRepository.getMovieCastById(movieId) } returns expectedCast

        // When
        getMovieCastUseCase(movieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getMovieCastById(movieId) }
    }
}
