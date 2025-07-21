package com.london.data.mapper.videoprovider.tvshow

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.remote.details.videoprovider.tvshow.model.TvShowVideoRemote
import com.london.domain.entity.videoprovider.TvShowVideo
import kotlin.test.Test

class TvShowVideoMapperKtTest {
    private val fakeRemoteVideo = TvShowVideoRemote(
        id = "vid123",
        iso31661 = "US",
        iso6391 = "en",
        key = "abcd1234",
        name = "Official Trailer",
        official = true,
        publishedAt = "2024-05-01",
        site = "YouTube",
        size = 1080,
        type = "Trailer"
    )

    private val fakeDomainVideo = TvShowVideo(
        id = "vid123",
        iso31661 = "US",
        iso6391 = "en",
        videoUrl = "https://www.youtube.com/watch?v=abcd1234",
        name = "Official Trailer",
        official = true,
        publishedAt = "2024-05-01",
        site = "YouTube",
        size = 1080,
        type = "Trailer"
    )

    @Test
    fun `TvShowVideoRemote maps to TvShowVideo correctly`() {
        val result = fakeRemoteVideo.toTvShowVideo()
        assertThat(result).isEqualTo(fakeDomainVideo)
    }

    @Test
    fun `TvShowVideo maps back to TvShowVideoRemote correctly`() {
        val result = fakeDomainVideo.TvShowVideoRemote()

        assertThat(result).isEqualTo(
            fakeRemoteVideo.copy(
                iso6391 = "en",
                key = "https://www.youtube.com/watch?v=abcd1234",

                )
        )
    }
}