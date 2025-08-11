package com.london.domain.usecase

import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import com.london.domain.usecase.details.actor.GetActorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetActorTvShowPicksByIdUseCaseTest {


    private lateinit var actorRepository: ActorRepository
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetActorUseCase

    @Before
    fun setup() {
        actorRepository = mockk()
        tvShowRepository = mockk()
        movieRepository = mockk()
        useCase = GetActorUseCase(
            actorRepository = actorRepository,
            tvShowRepository = tvShowRepository,
            movieRepository = movieRepository
        )
    }

    @Test
    fun `should call repository getActorTvShowPicksById with correct id and return result`() =
        runTest {
            // Given
            val actorId = 1
            val expectedResult = mockk<CastDetails>()
            coEvery { tvShowRepository.getActorTvShowPicksById(actorId) } returns expectedResult

            // When
            val result = useCase.getActorTvShowPicksById(actorId)

            // Then
            coVerify(exactly = 1) { tvShowRepository.getActorTvShowPicksById(actorId) }
            assertEquals(expectedResult, result)
        }
}