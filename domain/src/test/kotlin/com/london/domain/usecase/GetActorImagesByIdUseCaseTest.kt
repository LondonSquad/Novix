package com.london.domain.usecase

import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class GetActorImagesByIdUseCaseTest {

    private lateinit var repository: ActorRepository
    private lateinit var useCase: GetActorImagesByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetActorImagesByIdUseCase(repository)
    }

    @Test
    fun `should call repository getActorImagesById with correct id and return result`() = runTest {
        // Given
        val actorId = 1
        val expectedResult = mockk<ActorImageDetails>()
        coEvery { repository.getActorImagesById(actorId) } returns expectedResult

        // When
        val result = useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorImagesById(actorId) }
        assertEquals(expectedResult, result)
    }
}