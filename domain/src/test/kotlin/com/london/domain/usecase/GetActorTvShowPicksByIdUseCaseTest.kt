package com.london.domain.usecase

import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails
import com.london.domain.repository.ActorRepository
import com.london.domain.usecase.toppicks.GetActorTvShowPicksByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetActorTvShowPicksByIdUseCaseTest {

 private lateinit var repository: ActorRepository
 private lateinit var useCase: GetActorTvShowPicksByIdUseCase

 @Before
 fun setup() {
  repository = mockk()
  useCase = GetActorTvShowPicksByIdUseCase(repository)
 }

 @Test
 fun `should call repository getActorTvShowPicksById with correct id and return result`() = runTest {
  // Given
  val actorId = 1
  val expectedResult = mockk<ActorTvShowDetails>()
  coEvery { repository.getActorTvShowPicksById(actorId) } returns expectedResult

  // When
  val result = useCase.invoke(actorId)

  // Then
  coVerify(exactly = 1) { repository.getActorTvShowPicksById(actorId) }
  assertEquals(expectedResult, result)
 }
}