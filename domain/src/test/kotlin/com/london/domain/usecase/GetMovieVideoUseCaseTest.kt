package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.videoprovider.MovieVideo
import com.london.domain.repository.MovieVideoProviderRepository
import com.london.domain.usecase.details.movie.GetMovieVideoUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMovieVideoUseCaseTest {

    private lateinit var repository: MovieVideoProviderRepository
    private lateinit var getMovieVideoUseCase: GetMovieVideoUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getMovieVideoUseCase = GetMovieVideoUseCase(repository)
    }

    @Test
    fun `should return movie videos when repository returns videos`() = runTest {
        // given
        coEvery { repository.getMovieVideos(MOVIE_ID) } returns mockVideos

        // when
        val result = getMovieVideoUseCase.invoke(MOVIE_ID)

        // then
        assertThat(result[0]).isEqualTo(mockVideos[0])
        assertThat(result[1]).isEqualTo(mockVideos[1])
    }

    @Test
    fun `should return empty list when repository returns no videos`() = runTest {
        // given
        coEvery { repository.getMovieVideos(MOVIE_ID) } returns emptyList()

        // when
        val result = getMovieVideoUseCase.invoke(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        // given
        coEvery { repository.getMovieVideos(MOVIE_ID) } throws RuntimeException("Network error")

        // when // then
        assertThrows<RuntimeException> {
            getMovieVideoUseCase.invoke(MOVIE_ID)
        }
    }

    companion object {
        const val MOVIE_ID = 123

        val mockVideos = listOf(
            MovieVideo(
                id = "vid1",
                iso31661 = "US",
                iso6391 = "en",
                videoUrl = "https://youtube.com/watch?v=123",
                name = "Official Trailer",
                official = true,
                publishedAt = "2025-07-19",
                site = "YouTube",
                size = 1080,
                type = "Trailer"
            ), MovieVideo(
                id = "vid2",
                iso31661 = "US",
                iso6391 = "en",
                videoUrl = "https://youtube.com/watch?v=456",
                name = "Teaser",
                official = false,
                publishedAt = "2025-07-18",
                site = "YouTube",
                size = 720,
                type = "Teaser"
            )
        )
    }
}
