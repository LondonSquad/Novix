package com.london.domain.usecase

import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.domain.repository.MovieRepository
import com.london.domain.usecase.toppicks.GetActorMoviePicksByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetActorMoviePicksByIdUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetActorMoviePicksByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetActorMoviePicksByIdUseCase(repository)
    }

    @Test
    fun `should call repository getActorMoviePicksById with correct id and return result`() = runTest {
        // Given
        val actorId = 1
        val expectedResult = mockk<CastDetails>()
        coEvery { repository.getActorMoviePicksById(actorId) } returns expectedResult

        // When
        val result = useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorMoviePicksById(actorId) }
        assertEquals(expectedResult, result)
    }
}