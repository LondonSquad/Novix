package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.PopularRepository
import com.london.domain.repository.TrendingRepository
import com.london.domain.repository.TvShowRepository
import com.london.domain.repository.discover.DiscoverRepository
import com.london.domain.usecase.details.movie.ManageMovieUseCase
import com.london.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetMoviesByCategoryUseCaseTest {

    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var trendingRepository: TrendingRepository
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var discoverRepository: MovieRepository
    private lateinit var popularRepository: PopularRepository
    private lateinit var manageMovieUseCase: ManageMovieUseCase

    @Before
    fun setUp() {
        movieDetailsRepository = mockk()
        trendingRepository = mockk()
        tvShowRepository = mockk()
        discoverRepository = mockk()
        popularRepository = mockk()
        manageMovieUseCase = ManageMovieUseCase(
            trendingRepository = trendingRepository,
            popularRepository = popularRepository,
            discoverRepository = discoverRepository,
            movieRepository = movieDetailsRepository
        )
    }

    @Test
    fun `should return a paged fetch response of movies when repository successfully fetches movies`() =
        runTest {
            //given
            coEvery {
                discoverRepository.getMoviesByCategory(
                    CATEGORY_ID, PAGE_NUMBER
                )
            } returns pagedFetchResponse
            //when
            val result = manageMovieUseCase.getMoviesByCategory(CATEGORY_ID, PAGE_NUMBER)
            //then
            assertThat(result).isEqualTo(pagedFetchResponse)
        }

    private companion object {
        private const val CATEGORY_ID = 1
        private const val PAGE_NUMBER = 1
        private val movie = Movie(
            id = 1,
            name = "",
            posterUrl = "",
            releaseYear = 2024,
            rating = 8,
            genreIds = listOf(1, 2, 3)
        )
        private val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1, items = listOf(movie), totalPages = 1, totalItems = 1
        )
    }
}