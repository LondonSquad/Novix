package com.london.domain.usecase

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class GetActorDetailsByIdUseCaseTest {

    private lateinit var repository: ActorRepository
    private lateinit var useCase: GetActorDetailsByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetActorDetailsByIdUseCase(repository)
    }

    @Test
    fun `should call repository getActorDetailsById with correct id and return result`() = runTest {
        // Given
        val actorId = 1
        val expectedResult = mockk<ActorDetails>()
        coEvery { repository.getActorDetailsById(actorId) } returns expectedResult

        // When
        val result = useCase.invoke(actorId)

        // Then
        coVerify(exactly = 1) { repository.getActorDetailsById(actorId) }
        assertEquals(expectedResult, result)
    }
}