package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPopularMoviesTest {

    private lateinit var repository: MovieRepository
    private lateinit var getPopularMovies: GetPopularMovies

    @Before
    fun setUp() {
        repository = mockk()
        getPopularMovies = GetPopularMovies(repository)
    }

    @Test
    fun `when call invoke should return list of popular movies from repository`() = runTest {
        // Given
        val expectedMovies = listOf(
            PopularMedia(
                id = 1,
                name = "Movie One",
                posterUrl = "path1",
                rating = 7.5,
                mediaType = MediaType.Movie
            ),
            PopularMedia(
                id = 2,
                name = "Movie Two",
                rating = 8.3,
                posterUrl = "path2",
                mediaType = MediaType.Movie
            )
        )
        coEvery { repository.getPopularMovies() } returns expectedMovies

        // When
        val result = getPopularMovies.invoke()

        // Then
        assertThat(result).isEqualTo(expectedMovies)
        coVerify(exactly = 1) { repository.getPopularMovies() }
    }
}
