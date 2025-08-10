package com.london.domain.usecase

import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.TrendingRepository
import com.london.domain.usecase.details.actor.ManageActorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetActorImagesByIdUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var trendingRepository: TrendingRepository
    private lateinit var manageActorUseCase: ManageActorUseCase

    @Before
    fun setup() {
        actorRepository = mockk()
        trendingRepository = mockk()
        manageActorUseCase = ManageActorUseCase(
            actorRepository = actorRepository,
            trendingRepository = trendingRepository
        )
    }

    @Test
    fun `should call repository getActorImagesById with correct id and return profiles list`() =
        runTest {
            // Given
            val actorId = 1
            val mockImageDetails = listOf(
                mockk<ImageDetails>(),
                mockk<ImageDetails>()
            )
            val mockActorImageDetails = mockk<ActorImageDetails> {
                coEvery { profiles } returns mockImageDetails
            }
            coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

            // When
            val result = manageActorUseCase.getActorImagesById(actorId)

            // Then
            coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
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
        coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = manageActorUseCase.getActorImagesById(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
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
        coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = manageActorUseCase.getActorImagesById(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
        assertEquals(mockImageDetails, result)
    }
}