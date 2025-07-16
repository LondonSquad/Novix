package com.london.domain.usecase

import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorImagesByIdUseCaseTest {

    private lateinit var repository: ActorRepository
    private lateinit var useCase: GetActorImagesByIdUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = GetActorImagesByIdUseCase(repository)
    }

    @Test
    fun `should call repository getActorDetailsById with correct id`() = runTest {
        // Given
        val actorId = 1
        coEvery { repository.getActorImagesById(actorId) } returns mockk()

        // When
        useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorImagesById(actorId) }
    }
}