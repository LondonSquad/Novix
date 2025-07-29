package com.london.data.mapper.movie

import com.google.common.truth.Truth.assertThat
import com.london.data.mapper.videoprovider.movie.toMovie
import com.london.data.remote.model.details.videoprovider.movie.model.MovieVideoRemoteResponse
import com.london.domain.entity.videoprovider.MovieVideo
import org.junit.Test

class MovieVideoMapperKtTest {
    @Test
    fun `toMovie should map MovieVideoRemote to MovieVideo correctly`() {
        // Given
        val remoteVideo = fakeMovieVideoRemote()

        // When
        val domainVideo = remoteVideo.toMovie()

        // Then
        assertThat(domainVideo).isEqualTo(
            MovieVideo(
                id = "vid123",
                videoUrl = "https://www.youtube.com/watch?v=abc123",
                name = "Official Trailer",
                official = true,
                site = "YouTube",
            )
        )
    }

    private fun fakeMovieVideoRemote() = MovieVideoRemoteResponse(
        id = "vid123",
        iso31661 = "US",
        iso6391 = "en",
        key = "abc123",
        name = "Official Trailer",
        official = true,
        publishedAt = "2025-07-19",
        site = "YouTube",
        size = 1080,
        type = "Trailer"
    )
}