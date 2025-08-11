package com.london.domain.usecase

import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.TvShowRepository
import com.london.domain.usecase.details.tvshow.GetTvEpisodesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetEpisodesByTvShowSeasonTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var actorRepository: ActorRepository
    private lateinit var getTvEpisodesUseCase: GetTvEpisodesUseCase

    @Before
    fun setup() {
        tvShowRepository = mockk()
        actorRepository = mockk()
        getTvEpisodesUseCase = GetTvEpisodesUseCase(
            tvShowRepository = tvShowRepository,
            actorRepository = actorRepository
        )
    }

    @Test
    fun `should call repository getTvShowEpisodesBySeason with correct parameters and return result`() = runTest {
        // Given
        val tvShowId = 123
        val seasonNumber = 2
        val expectedResult = mockk<TvShowEpisodesEntity>()
        coEvery {
            tvShowRepository.getTvShowEpisodesBySeason(
                tvShowId,
                seasonNumber
            )
        } returns expectedResult

        // When
        val result = getTvEpisodesUseCase.getTvShowEpisodesBySeason(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) }
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should work with different tvShowId and seasonNumber combinations`() = runTest {
        // Given
        val tvShowId = 456
        val seasonNumber = 1
        val expectedResult = mockk<TvShowEpisodesEntity>()
        coEvery {
            tvShowRepository.getTvShowEpisodesBySeason(
                tvShowId,
                seasonNumber
            )
        } returns expectedResult

        // When
        val result = getTvEpisodesUseCase.getTvShowEpisodesBySeason(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) }
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should work with season number zero`() = runTest {
        // Given
        val tvShowId = 789
        val seasonNumber = 0
        val expectedResult = mockk<TvShowEpisodesEntity>()
        coEvery {
            tvShowRepository.getTvShowEpisodesBySeason(
                tvShowId,
                seasonNumber
            )
        } returns expectedResult

        // When
        val result = getTvEpisodesUseCase.getTvShowEpisodesBySeason(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) }
        assertEquals(expectedResult, result)
    }
}