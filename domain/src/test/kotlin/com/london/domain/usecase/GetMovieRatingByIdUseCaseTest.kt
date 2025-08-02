package com.london.domain.usecase

import com.london.domain.entity.moviedatails.MovieStates
import com.london.domain.repository.MovieDetailsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetMovieRatingByIdUseCaseTest {

    private lateinit var repository: MovieDetailsRepository
    private lateinit var useCase: GetMovieRatingByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMovieRatingByIdUseCase(repository)
    }

    @Test
    fun `given valid movie id when invoked then returns movie rate`() = runTest {
        // Given
        val movieId = 123
        val expectedRate = 8
        val movieStates = mockMovieStates()

        coEvery { repository.getAccountMovieStatesById(movieId) } returns movieStates

        // When
        val result = useCase(movieId)

        // Then
        assertEquals(expectedRate, result)
    }

    private fun mockMovieStates() = MovieStates(
        id = 123,
        rate = 8,
        favorite = false,
        watchlist = true
    )
}
