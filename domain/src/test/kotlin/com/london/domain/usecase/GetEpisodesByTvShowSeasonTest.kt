package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class GetEpisodesByTvShowSeasonTest {

    private lateinit var repository: DetailsRepository
    private lateinit var useCase: GetEpisodesByTvShowSeason

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetEpisodesByTvShowSeason(repository)
    }

    @Test
    fun `should call repository getTvShowEpisodesBySeason with correct parameters and return result`() = runTest {
        // Given
        val tvShowId = 123
        val seasonNumber = 2
        val expectedResult = mockk<TvShowEpisodesEntity>()
        coEvery { repository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) } returns expectedResult

        // When
        val result = useCase.invoke(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { repository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) }
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should work with different tvShowId and seasonNumber combinations`() = runTest {
        // Given
        val tvShowId = 456
        val seasonNumber = 1
        val expectedResult = mockk<TvShowEpisodesEntity>()
        coEvery { repository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) } returns expectedResult

        // When
        val result = useCase.invoke(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { repository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) }
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should work with season number zero`() = runTest {
        // Given
        val tvShowId = 789
        val seasonNumber = 0
        val expectedResult = mockk<TvShowEpisodesEntity>()
        coEvery { repository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) } returns expectedResult

        // When
        val result = useCase.invoke(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { repository.getTvShowEpisodesBySeason(tvShowId, seasonNumber) }
        assertEquals(expectedResult, result)
    }
}