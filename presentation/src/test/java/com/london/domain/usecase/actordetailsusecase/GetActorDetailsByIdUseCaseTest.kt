package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorDetailsByIdUseCaseTest {
    lateinit var repository: ActorRepository
    lateinit var useCase: GetActorDetailsByIdUseCase
    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetActorDetailsByIdUseCase(repository)
    }
    
    @Test
    fun `should call the repository get actor details by id`() = runTest {
        //given
        coEvery { repository.getActorDetailsById(1) } returns mockk()
        //when
        useCase.invoke(1)
        //then
        repository.getActorDetailsById(1)
    }

}