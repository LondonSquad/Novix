package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.repository.PopularRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPopularMoviesTest {

    private lateinit var repository: PopularRepository
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
            PopularMovie(id = 1, title = "Movie One", posterUrl = "path1", rating = 7.5),
            PopularMovie(id = 2, title = "Movie Two", posterUrl = "path2", rating = 8.3)
        )
        coEvery { repository.getPopularMovies() } returns expectedMovies

        // When
        val result = getPopularMovies.invoke()

        // Then
        assertThat(result).isEqualTo(expectedMovies)
        coVerify(exactly = 1) { repository.getPopularMovies() }
    }
}
