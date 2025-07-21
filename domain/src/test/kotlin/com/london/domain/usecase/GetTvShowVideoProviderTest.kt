package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.videoprovider.TvShowVideo
import com.london.domain.repository.TvShowVideoProviderRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetTvShowVideoProviderTest {

    private lateinit var repository: TvShowVideoProviderRepository
    private lateinit var getTvShowVideoProvider: GetTvShowVideoProvider

    @Before
    fun setUp() {
        repository = mockk()
        getTvShowVideoProvider = GetTvShowVideoProvider(repository)
    }

    @Test
    fun `should return tv show videos when repository returns videos`() = runTest {
        // given
        coEvery { repository.getTvShowVideos(TV_SHOW_ID) } returns mockVideos

        // when
        val result = getTvShowVideoProvider.invoke(TV_SHOW_ID)

        // then
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(mockVideos[0])
        assertThat(result[1]).isEqualTo(mockVideos[1])
    }

    @Test
    fun `should return empty list when repository returns no videos`() = runTest {
        // given
        coEvery { repository.getTvShowVideos(TV_SHOW_ID) } returns emptyList()

        // when
        val result = getTvShowVideoProvider.invoke(TV_SHOW_ID)

        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        // given
        coEvery { repository.getTvShowVideos(TV_SHOW_ID) } throws RuntimeException("Network error")

        // when && then
        assertThrows<RuntimeException> {
            getTvShowVideoProvider.invoke(TV_SHOW_ID)
        }
    }

    private companion object {
        const val TV_SHOW_ID = 999

        val mockVideos = listOf(
            TvShowVideo(
                id = "vid1",
                iso31661 = "US",
                iso6391 = "en",
                videoUrl = "https://youtube.com/vid1",
                name = "Episode 1 Trailer",
                official = true,
                publishedAt = "2024-07-01",
                site = "YouTube",
                size = 1080,
                type = "Trailer"
            ),
            TvShowVideo(
                id = "vid2",
                iso31661 = "US",
                iso6391 = "en",
                videoUrl = "https://youtube.com/vid2",
                name = "Episode 2 Teaser",
                official = false,
                publishedAt = "2024-07-05",
                site = "YouTube",
                size = 720,
                type = "Teaser"
            )
        )
    }
}
