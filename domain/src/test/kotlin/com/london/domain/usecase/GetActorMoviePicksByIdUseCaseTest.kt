package com.london.domain.usecase

import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetActorMoviePicksByIdUseCaseTest {

    private lateinit var repository: ActorRepository
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
        val expectedResult = mockk<ActorMovieDetails>()
        coEvery { repository.getActorMoviePicksById(actorId) } returns expectedResult

        // When
        val result = useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorMoviePicksById(actorId) }
        assertEquals(expectedResult, result)
    }
}