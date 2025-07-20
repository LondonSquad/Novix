package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.remote.details.videoprovider.movie.model.MovieVideoProviderRemote
import com.london.data.datasource.remote.details.videoprovider.movie.model.MovieVideoRemote
import com.london.data.datasource.remote.details.videoprovider.movie.model.MovieVideoResponse
import com.london.domain.entity.videoprovider.MovieVideo
import com.london.domain.repository.MovieVideoProviderRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieVideoProviderRepositoryImplTest {

    private lateinit var movieVideoProviderRemote: MovieVideoProviderRemote
    private lateinit var repository: MovieVideoProviderRepository

    @Before
    fun setup() {
        movieVideoProviderRemote = mockk()
        repository = MovieVideoProviderRepositoryImpl(movieVideoProviderRemote)
    }

    @Test
    fun `getMovieVideos should return mapped MovieVideo list`() = runTest {
        // GIVEN
        coEvery { movieVideoProviderRemote.getMovieVideos(123) } returns MOVIE_VIDEO_LIST_REMOTE

        // WHEN
        val result = repository.getMovieVideos(123)

        // THEN
        assertThat(result).isEqualTo(EXPECTED_MOVIE_VIDEOS)
    }

    @Test
    fun `getMovieVideos should return empty list when movies is null`() = runTest {
        // GIVEN
        val emptyResponse = MovieVideoResponse(
            id = 1, movies = null
        )
        coEvery { movieVideoProviderRemote.getMovieVideos(999) } returns emptyResponse

        // WHEN
        val result = repository.getMovieVideos(999)

        // THEN
        assertThat(result).isEmpty()
    }

    companion object {
        private val REMOTE_VIDEO = MovieVideoRemote(
            id = "vid123",
            iso6391 = "en",
            iso31661 = "US",
            key = "abcd1234",
            name = "Official Trailer",
            site = "YouTube",
            size = 1080,
            type = "Trailer",
            official = true,
            publishedAt = "2024-05-01"
        )

        private val MOVIE_VIDEO_LIST_REMOTE = MovieVideoResponse(
            id = 1, movies = listOf(REMOTE_VIDEO)
        )

        private val EXPECTED_MOVIE_VIDEOS = listOf(
            MovieVideo(
                id = "vid123",
                iso6391 = "en",
                iso31661 = "US",
                videoUrl = "https://www.youtube.com/watch?v=abcd1234",
                name = "Official Trailer",
                site = "YouTube",
                size = 1080,
                type = "Trailer",
                official = true,
                publishedAt = "2024-05-01"
            )
        )
    }
}
