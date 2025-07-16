package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorMoviePicksByIdUseCaseTest {
    lateinit var repository: ActorRepository
    lateinit var useCase: GetActorMoviePicksByIdUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetActorMoviePicksByIdUseCase(repository)
    }

    @Test
    fun `should call the repository get actor movie picks by id`() = runTest {
        //given
        coEvery { repository.getActorMoviePicksById(1) } returns mockk()
        //when
        useCase.invoke(1)
        //then
        coEvery { repository.getActorMoviePicksById(1) }
    }

}