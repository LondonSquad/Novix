package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetEpisodeByTvShowIdTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getEpisodeByTvShowId: GetEpisodeByTvShowId

    @Before
    fun setUp() {
        tvShowRepository = mockk()
        getEpisodeByTvShowId = GetEpisodeByTvShowId(tvShowRepository)
    }

    @Test
    fun `should return episode when repository returns episode`() = runTest {
        // Given
        coEvery {
            tvShowRepository.getTvShowEpisodeByPosition(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns mockEpisode

        // When
        val result = getEpisodeByTvShowId(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

        // Then
        assertThat(result).isEqualTo(mockEpisode)
    }

    @Test
    fun `should throw exception when repository throws`() = runTest {
        // Given
        coEvery {
            tvShowRepository.getTvShowEpisodeByPosition(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } throws RuntimeException("Network error")

        // When / Then
        assertThrows<RuntimeException> {
            getEpisodeByTvShowId(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
        }
    }

    private companion object {
        const val TV_SHOW_ID = 12345
        const val SEASON_NUMBER = 1
        const val EPISODE_NUMBER = 1

        val mockEpisode = TvShowEpisodeByIdEntity(
            id = 1001,
            name = "Pilot",
            overview = "The very first episode.",
            voteAverage = 8.5,
            voteCount = 200,
            airDate = "2025-07-01",
            episodeNumber = EPISODE_NUMBER,
            seasonNumber = SEASON_NUMBER,
            stillPath = "/still_pilot.jpg",
            guestStars = emptyList(),
            episodeTypes = "Regular",
            tvShowId = 23,
        )
    }
}
