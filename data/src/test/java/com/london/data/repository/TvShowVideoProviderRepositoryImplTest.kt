package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.source.details.videoprovider.tvshow.TvShowVideoProviderRemote
import com.london.data.mapper.videoprovider.tvshow.toTvShowVideo
import com.london.data.remote.model.details.videoprovider.tvshow.model.TvShowVideoRemote
import com.london.data.remote.model.details.videoprovider.tvshow.model.TvShowVideoResponse
import com.london.domain.entity.videoprovider.TvShowVideo

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TvShowVideoProviderRepositoryImplTest {

    private lateinit var remoteDataSource: TvShowVideoProviderRemote
    private lateinit var repository: TvShowVideoProviderRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = TvShowVideoProviderRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getTvShowVideos should map remote video list correctly`() = runTest {
        // Given
        val tvShowId = 123
        coEvery { remoteDataSource.getTvShowVideos(tvShowId) } returns Result.success(
            fakeTvShowVideosResponse()
        )

        // When
        val result: List<TvShowVideo> = repository.getTvShowVideos(tvShowId)

        // Then
        assertThat(result).hasSize(2)

        val firstVideo = result.first()
        assertThat(firstVideo).isEqualTo(fakeTvShowVideosResponse().tvShow?.get(0)?.toTvShowVideo())

        val secondVideo = result[1]
        assertThat(secondVideo).isEqualTo(
            fakeTvShowVideosResponse().tvShow?.get(1)?.toTvShowVideo()
        )
    }

    @Test
    fun `getTvShowVideos should return empty list when API returns null list`() = runTest {
        // Given
        val tvShowId = 999
        coEvery { remoteDataSource.getTvShowVideos(tvShowId) } returns Result.success(
            fakeNullTvShowVideosResponse()
        )

        // When
        val result = repository.getTvShowVideos(tvShowId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowVideos should throw ValidationException when remote fails`() = runTest {

        val tvShowId = 123
        coEvery {
            remoteDataSource.getTvShowVideos(tvShowId)
        } throws NetworkException.ValidationException("validation error")

        assertThrows<NetworkException.ValidationException> {
            repository.getTvShowVideos(tvShowId)
        }
    }


    private fun fakeTvShowVideosResponse() = TvShowVideoResponse(
        id = 1,
        tvShow = listOf(
            TvShowVideoRemote(
                id = "vid1",
                iso31661 = "US",
                iso6391 = "en",
                key = "123",
                name = "Official Trailer",
                official = true,
                publishedAt = "2025-07-19",
                site = "YouTube",
                size = 1080,
                type = "Trailer"
            ),
            TvShowVideoRemote(
                id = "vid2",
                iso31661 = "US",
                iso6391 = "en",
                key = "456",
                name = "Teaser",
                official = false,
                publishedAt = "2025-07-18",
                site = "YouTube",
                size = 720,
                type = "Teaser"
            )
        )
    )

    private fun fakeNullTvShowVideosResponse() = TvShowVideoResponse(
        id = 999,
        tvShow = null
    )
}
