package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorImagesByIdUseCaseTest {
    lateinit var repository: ActorRepository
    lateinit var useCase: GetActorImagesByIdUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetActorImagesByIdUseCase(repository)
    }

    @Test
    fun `should call the repository get actor images by id`() = runTest {
        //given
        coEvery { repository.getActorImagesById(1) } returns mockk()
        //when
        useCase.invoke(1)
        //then
        coEvery { repository.getActorImagesById(1) }
    }
}