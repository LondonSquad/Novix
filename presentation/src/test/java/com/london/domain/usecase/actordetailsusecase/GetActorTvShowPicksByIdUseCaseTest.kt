package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorTvShowPicksByIdUseCaseTest {
    lateinit var repository: ActorRepository
    lateinit var useCase: GetActorTvShowPicksByIdUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetActorTvShowPicksByIdUseCase(repository)
    }
    
    @Test
    fun `should call the repository get actor tv show picks by id`() = runTest {
        //given
        coEvery { repository.getActorTvShowPicksById(1) } returns mockk()
        //when
        useCase.invoke(1)
        //then
        repository.getActorTvShowPicksById(1)
    }
}