package com.london.domain.usecase

import com.london.domain.entity.actordetails.ActorImageDetails
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import com.london.domain.usecase.details.actor.GetActorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetActorImagesByIdUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var movieRepository: MovieRepository
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getActorUseCase: GetActorUseCase

    @Before
    fun setup() {
        actorRepository = mockk()
        getActorUseCase = GetActorUseCase(
            actorRepository = actorRepository,
            movieRepository = movieRepository,
            tvShowRepository = tvShowRepository
        )
    }

    @Test
    fun `should call repository getActorImagesById with correct id and return profiles list`() =
        runTest {
            // Given
            val actorId = 1
            val mockImageDetails = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg"
            )
            val mockActorImageDetails = mockk<ActorImageDetails> {
                coEvery { imageUrl } returns mockImageDetails
            }
            coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

            // When
            val result = getActorUseCase.getActorImagesById(actorId)

            // Then
            coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
            assertEquals(mockImageDetails, result)
        }

    @Test
    fun `should return empty list when profiles is empty`() = runTest {
        // Given
        val actorId = 1
        val emptyImageDetails = emptyList<String>()
        val mockActorImageDetails = mockk<ActorImageDetails> {
            coEvery { imageUrl } returns emptyImageDetails
        }
        coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = getActorUseCase.getActorImagesById(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
        assertEquals(emptyImageDetails, result)
        assertEquals(0, result.size)
    }

    @Test
    fun `should handle different actor ids correctly`() = runTest {
        // Given
        val actorId = 999
        val mockImageDetails = listOf(
            "https://test.com/image1.jpg",
            "https://test.com/image2.jpg"
        )
        val mockActorImageDetails = mockk<ActorImageDetails> {
            coEvery { imageUrl } returns mockImageDetails
        }
        coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = getActorUseCase.getActorImagesById(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
        assertEquals(mockImageDetails, result)
    }
}
