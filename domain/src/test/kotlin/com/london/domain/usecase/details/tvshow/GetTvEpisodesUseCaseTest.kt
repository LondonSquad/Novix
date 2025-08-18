package com.london.domain.usecase.details.tvshow

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.tvshow.TvShowCast
import com.london.domain.entity.tvshow.TvShowCastMember
import com.london.domain.entity.tvshow.cast.TvShowRole
import com.london.domain.entity.tvshow.episode.EpisodeDetails
import com.london.domain.entity.tvshow.episode.SeasonEpisodes
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetTvEpisodesUseCaseTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var actorRepository: ActorRepository
    private lateinit var getTvEpisodesUseCase: GetTvEpisodesUseCase
    private lateinit var searchRepository: SearchRepository
    private lateinit var gettTvShowUseCase: GetTvShowUseCase

    @Before
    fun setUp() {
        tvShowRepository = mockk()
        actorRepository = mockk()
        searchRepository = mockk()
        getTvEpisodesUseCase = GetTvEpisodesUseCase(
            tvShowRepository = tvShowRepository,
        )
        gettTvShowUseCase = GetTvShowUseCase(
            tvShowRepository = tvShowRepository,
            searchRepository = searchRepository,
            actorRepository = actorRepository
        )
    }

    @Test
    fun `should return episode when repository returns episode`() = runTest {
        // Given
        coEvery {
            tvShowRepository.getTvShowEpisodeByPosition(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns mockEpisode

        // When
        val result =
            getTvEpisodesUseCase.getEpisodeByTvShowId(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

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
            getTvEpisodesUseCase.getEpisodeByTvShowId(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
        }
    }


    @Test
    fun `should call repository getTvShowEpisodesBySeason with correct parameters and return result`() =
        runTest {
            // Given
            val tvShowId = 123
            val seasonNumber = 2
            val expectedResult = mockk<SeasonEpisodes>()
            coEvery {
                tvShowRepository.getTvShowSeasonEpisodes(
                    tvShowId,
                    seasonNumber
                )
            } returns expectedResult

            // When
            val result = getTvEpisodesUseCase.getTvShowSeasonEpisodes(tvShowId, seasonNumber)

            // Then
            coVerify(exactly = 1) {
                tvShowRepository.getTvShowSeasonEpisodes(
                    tvShowId,
                    seasonNumber
                )
            }
            Assert.assertEquals(expectedResult, result)
        }

    @Test
    fun `should work with different tvShowId and seasonNumber combinations`() = runTest {
        // Given
        val tvShowId = 456
        val seasonNumber = 1
        val expectedResult = mockk<SeasonEpisodes>()
        coEvery {
            tvShowRepository.getTvShowSeasonEpisodes(
                tvShowId,
                seasonNumber
            )
        } returns expectedResult

        // When
        val result = getTvEpisodesUseCase.getTvShowSeasonEpisodes(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowSeasonEpisodes(tvShowId, seasonNumber) }
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `should work with season number zero`() = runTest {
        // Given
        val tvShowId = 789
        val seasonNumber = 0
        val expectedResult = mockk<SeasonEpisodes>()
        coEvery {
            tvShowRepository.getTvShowSeasonEpisodes(
                tvShowId,
                seasonNumber
            )
        } returns expectedResult

        // When
        val result = getTvEpisodesUseCase.getTvShowSeasonEpisodes(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowSeasonEpisodes(tvShowId, seasonNumber) }
        Assert.assertEquals(expectedResult, result)
    }


    @Test
    fun `invoke should return list of video URLs when repository succeeds`() = runTest {
        // Given
        val seriesId = 123
        val seasonNumber = 1
        val episodeNumber = 5
        val expectedVideoUrls = listOf(
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            "https://www.youtube.com/watch?v=abc123def456"
        )

        coEvery {
            getTvEpisodesUseCase.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedVideoUrls

        // When
        val result = getTvEpisodesUseCase.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEqualTo(expectedVideoUrls)
        coVerify(exactly = 1) {
            tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        }
    }

    @Test
    fun `invoke should return empty list when repository returns empty list`() = runTest {
        // Given
        val seriesId = 123
        val seasonNumber = 1
        val episodeNumber = 5
        val expectedEmptyList = emptyList<String>()

        coEvery {
            tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedEmptyList

        // When
        val result = getTvEpisodesUseCase.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEmpty()
        assertThat(result).isEqualTo(expectedEmptyList)
        coVerify(exactly = 1) {
            tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        }
    }

    @Test
    fun `invoke should propagate exception when repository throws exception`() = runTest {
        // Given
        val seriesId = 123
        val seasonNumber = 1
        val episodeNumber = 5
        val expectedException = RuntimeException("Network error")

        coEvery {
            tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } throws expectedException

        // When & Then
        val actualException = assertThrows<RuntimeException> {
            getTvEpisodesUseCase.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        }

        assertThat(actualException).isEqualTo(expectedException)
        assertThat(actualException.message).isEqualTo("Network error")
        coVerify(exactly = 1) {
            tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters`() = runTest {
        // Given
        val seriesId = 456
        val seasonNumber = 2
        val episodeNumber = 10
        val expectedVideoUrls = listOf("https://www.youtube.com/watch?v=test123")

        coEvery {
            tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedVideoUrls

        // When
        getTvEpisodesUseCase.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)

        // Then
        coVerify(exactly = 1) {
            tvShowRepository.getEpisodeVideos(
                tvShowId = 456,
                seasonNumber = 2,
                episodeNumber = 10
            )
        }
    }

    @Test
    fun `invoke should handle null or invalid parameters correctly`() = runTest {
        // Given
        val seriesId = 0
        val seasonNumber = -1
        val episodeNumber = 0
        val expectedVideoUrls = emptyList<String>()

        coEvery {
            tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedVideoUrls

        // When
        val result = getTvEpisodesUseCase.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) {
            tvShowRepository.getEpisodeVideos(
                tvShowId = 0,
                seasonNumber = -1,
                episodeNumber = 0
            )
        }
    }

    @Test
    fun `invoke should work with large parameter values`() = runTest {
        // Given
        val seriesId = Int.MAX_VALUE
        val seasonNumber = 999
        val episodeNumber = 999
        val expectedVideoUrls = listOf("https://www.youtube.com/watch?v=largeValue")

        coEvery {
            tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedVideoUrls

        // When
        val result = getTvEpisodesUseCase.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEqualTo(expectedVideoUrls)
        coVerify(exactly = 1) {
            tvShowRepository.getEpisodeVideos(
                tvShowId = Int.MAX_VALUE,
                seasonNumber = 999,
                episodeNumber = 999
            )
        }
    }

    @Test
    fun `should return cast when repository returns cast`() = runTest {
        //given
        coEvery { actorRepository.getTvShowActors(TV_SHOW_ID) } returns mockCast
        //when
        val result = gettTvShowUseCase.getTvShowCastById(TV_SHOW_ID)
        //then
        assertThat(result).isEqualTo(mockCast)
    }

    private companion object {
        const val TV_SHOW_ID = 12345
        const val SEASON_NUMBER = 1
        const val EPISODE_NUMBER = 1

        val mockCast = TvShowCast(
            cast = listOf(
                TvShowCastMember(
                    id = 1,
                    name = "John Doe",
                    profileUrl = "/profile1.jpg",
                    roles = listOf(
                        TvShowRole(
                            character = "Main Character",
                            episodeCount = 24
                        )
                    ),
                ),
                TvShowCastMember(
                    id = 2,
                    name = "Jane Smith",
                    profileUrl = "/profile2.jpg",
                    roles = listOf(
                        TvShowRole(
                            character = "Supporting Character",
                            episodeCount = 18
                        )
                    ),
                )
            ),
            id = TV_SHOW_ID
        )
    }

    private val mockEpisode = EpisodeDetails(
        id = 1001,
        name = "Pilot",
        overview = "The very first episode.",
        voteAverage = 8.5,
        voteCount = 200,
        airDate = "2025-07-01",
        seasonNumber = SEASON_NUMBER,
        guestStars = emptyList(),
        tvShowId = 23,
    )
}
