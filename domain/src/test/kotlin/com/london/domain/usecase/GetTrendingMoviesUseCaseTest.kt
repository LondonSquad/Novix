package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.trending.Trending
import com.london.domain.repository.TrendingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetTrendingMoviesUseCaseTest {
    private lateinit var trendingRepository: TrendingRepository
    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase

    @Before
    fun setUp() {
        trendingRepository = mockk()
        getTrendingMoviesUseCase = GetTrendingMoviesUseCase(trendingRepository)
    }

    @Test
    fun `should return paged fetch response when repository returns paged fetch response`() = runTest {
        // given
        coEvery { trendingRepository.getTrendingMovies(PAGE_NUMBER) } returns pagedFetchResponse
        // when
        val result = getTrendingMoviesUseCase(PAGE_NUMBER)
        // then
        assertThat(result).isEqualTo(pagedFetchResponse)
    }

    @Test
    fun `should return empty response when repository returns empty list`() = runTest {
        // given
        coEvery { trendingRepository.getTrendingMovies(PAGE_NUMBER) } returns emptyPagedFetchResponse
        // when
        val result = getTrendingMoviesUseCase(PAGE_NUMBER)
        // then
        assertThat(result.items).isEmpty()
        assertThat(result.totalItems).isEqualTo(0)
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        // given
        coEvery { trendingRepository.getTrendingMovies(PAGE_NUMBER) } throws RuntimeException("Network error")
        // when & then
        assertThrows<RuntimeException> {
            getTrendingMoviesUseCase(PAGE_NUMBER)
        }
    }

    private companion object {
        const val PAGE_NUMBER = 1
        val trending = Trending(
            id = 1,
            posterPath = "",
            genreIds = listOf(1, 2, 3)
        )
        val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1,
            items = listOf(trending),
            totalPages = 1,
            totalItems = 1
        )
        val emptyPagedFetchResponse = PagedFetchResponse<Trending>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 1,
            totalItems = 0
        )
    }
} 