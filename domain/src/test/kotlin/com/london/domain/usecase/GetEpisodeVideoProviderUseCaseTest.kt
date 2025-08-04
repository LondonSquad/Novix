package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetEpisodeVideoProviderUseCaseTest {

    private lateinit var repository: TvShowRepository
    private lateinit var useCase: GetEpisodeVideoProviderUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = GetEpisodeVideoProviderUseCase(repository)
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
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedVideoUrls

        // When
        val result = useCase.invoke(seriesId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEqualTo(expectedVideoUrls)
        coVerify(exactly = 1) {
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
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
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedEmptyList

        // When
        val result = useCase.invoke(seriesId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEmpty()
        assertThat(result).isEqualTo(expectedEmptyList)
        coVerify(exactly = 1) {
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
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
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } throws expectedException

        // When & Then
        val actualException = assertThrows<RuntimeException> {
            useCase.invoke(seriesId, seasonNumber, episodeNumber)
        }

        assertThat(actualException).isEqualTo(expectedException)
        assertThat(actualException.message).isEqualTo("Network error")
        coVerify(exactly = 1) {
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
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
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedVideoUrls

        // When
        useCase.invoke(seriesId, seasonNumber, episodeNumber)

        // Then
        coVerify(exactly = 1) {
            repository.getEpisodeVideos(
                seriesId = 456,
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
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedVideoUrls

        // When
        val result = useCase.invoke(seriesId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) {
            repository.getEpisodeVideos(
                seriesId = 0,
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
            repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
        } returns expectedVideoUrls

        // When
        val result = useCase.invoke(seriesId, seasonNumber, episodeNumber)

        // Then
        assertThat(result).isEqualTo(expectedVideoUrls)
        coVerify(exactly = 1) {
            repository.getEpisodeVideos(
                seriesId = Int.MAX_VALUE,
                seasonNumber = 999,
                episodeNumber = 999
            )
        }
    }
}