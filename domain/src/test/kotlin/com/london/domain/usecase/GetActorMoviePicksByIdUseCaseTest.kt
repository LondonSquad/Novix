package com.london.domain.usecase

import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorMoviePicksByIdUseCaseTest {

    private lateinit var repository: ActorRepository
    private lateinit var useCase: GetActorMoviePicksByIdUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = GetActorMoviePicksByIdUseCase(repository)
    }

    @Test
    fun `should call repository getActorDetailsById with correct id`() = runTest {
        // Given
        val actorId = 1
        coEvery { repository.getActorMoviePicksById(actorId) } returns mockk()

        // When
        useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorMoviePicksById(actorId) }
    }
}
