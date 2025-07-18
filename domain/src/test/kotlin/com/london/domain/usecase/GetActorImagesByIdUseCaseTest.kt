package com.london.domain.usecase

import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetActorImagesByIdUseCaseTest {

    private lateinit var repository: ActorRepository
    private lateinit var useCase: GetActorImagesByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetActorImagesByIdUseCase(repository)
    }

    @Test
    fun `should call repository getActorImagesById with correct id and return profiles list`() = runTest {
        // Given
        val actorId = 1
        val mockImageDetails = listOf(
            mockk<ImageDetails>(),
            mockk<ImageDetails>()
        )
        val mockActorImageDetails = mockk<ActorImageDetails> {
            coEvery { profiles } returns mockImageDetails
        }
        coEvery { repository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorImagesById(actorId) }
        assertEquals(mockImageDetails, result)
    }

    @Test
    fun `should return empty list when profiles is empty`() = runTest {
        // Given
        val actorId = 1
        val emptyImageDetails = emptyList<ImageDetails>()
        val mockActorImageDetails = mockk<ActorImageDetails> {
            coEvery { profiles } returns emptyImageDetails
        }
        coEvery { repository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorImagesById(actorId) }
        assertEquals(emptyImageDetails, result)
        assertEquals(0, result.size)
    }

    @Test
    fun `should handle different actor ids correctly`() = runTest {
        // Given
        val actorId = 999
        val mockImageDetails = listOf(mockk<ImageDetails>())
        val mockActorImageDetails = mockk<ActorImageDetails> {
            coEvery { profiles } returns mockImageDetails
        }
        coEvery { repository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorImagesById(actorId) }
        assertEquals(mockImageDetails, result)
    }
}