package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.PopularRepository
import com.london.domain.repository.TrendingRepository
import com.london.domain.repository.discover.DiscoverRepository
import com.london.domain.usecase.details.movie.ManageMovieUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPopularMoviesTest {

    private lateinit var popularRepository: PopularRepository
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var discoverRepository: DiscoverRepository
    private lateinit var trendingRepository: TrendingRepository
    private lateinit var manageMovieUseCase: ManageMovieUseCase

    @Before
    fun setUp() {
        movieDetailsRepository = mockk()
        popularRepository = mockk()
        discoverRepository = mockk()
        trendingRepository = mockk()
        manageMovieUseCase = ManageMovieUseCase(
            movieRepository = movieDetailsRepository,
            trendingRepository = trendingRepository,
            popularRepository = popularRepository,
            discoverRepository = discoverRepository
        )
    }

    @Test
    fun `when call invoke should return list of popular movies from repository`() = runTest {
        // Given
        val expectedMovies = listOf(
            PopularMovie(id = 1, title = "Movie One", posterUrl = "path1", rating = 7.5),
            PopularMovie(id = 2, title = "Movie Two", posterUrl = "path2", rating = 8.3)
        )
        coEvery { popularRepository.getPopularMovies() } returns expectedMovies

        // When
        val result = manageMovieUseCase.getPopularMovies()

        // Then
        assertThat(result).isEqualTo(expectedMovies)
        coVerify(exactly = 1) { popularRepository.getPopularMovies() }
    }
}
