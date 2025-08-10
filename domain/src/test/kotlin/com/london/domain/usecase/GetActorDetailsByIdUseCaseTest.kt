package com.london.domain.usecase

import com.london.domain.entity.actordetails.ActorDetails
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

class GetActorDetailsByIdUseCaseTest {

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
    fun `should call repository getActorDetailsById with correct id and return result`() = runTest {
        // Given
        val actorId = 1
        val expectedResult = mockk<ActorDetails>()
        coEvery { actorRepository.getActorDetailsById(actorId) } returns expectedResult

        // When
        val result = manageActorUseCase.getActorDetailsById(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorDetailsById(actorId) }
        assertEquals(expectedResult, result)
    }
}