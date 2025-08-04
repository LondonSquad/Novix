package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.toprated.TopRatedTvSeriesRepository
import com.london.domain.usecase.toprated.GetTopRatedTvSeriesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedTvSeriesUseCaseTest {

    private lateinit var repository: TopRatedTvSeriesRepository
    private lateinit var getTopRatedTvSeries: GetTopRatedTvSeriesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getTopRatedTvSeries = GetTopRatedTvSeriesUseCase(repository)
    }

    @Test
    fun `should return TV series when repository returns valid response`() = runTest {
        // Given
        val mockPagedResponse = PagedFetchResponse(
            items = mockTopRatedTvSeries,
            currentPage =  PAGE,
            totalPages = 2,
            totalItems = mockTopRatedTvSeries.size
        )

        coEvery {
            repository.getTopRatedTvSeries(PAGE)
        } returns mockPagedResponse

        // When
        val result = getTopRatedTvSeries(PAGE)

        // Then
        assertThat(result).isEqualTo(mockPagedResponse)

    }

    @Test
    fun `should return empty list when repository returns empty response`() = runTest {
        // Given
        val emptyPagedResponse = PagedFetchResponse(
            items = emptyList<TopRatedTvSeries>(),
            totalPages = PAGE,
            currentPage = 1,
            totalItems = 0
        )

        coEvery {
            repository.getTopRatedTvSeries(PAGE)
        } returns emptyPagedResponse

        // When
        val result = getTopRatedTvSeries(PAGE)

        // Then
        assertThat(result.items).isEmpty()
        assertThat(result.currentPage).isEqualTo(PAGE)
    }

    @Test
    fun `should throw RuntimeException when repository throws`() = runTest {
        // Given
        coEvery {
            repository.getTopRatedTvSeries(PAGE)
        } throws RuntimeException("Something went wrong")

        // When & Then
        assertThrows<RuntimeException> {
            getTopRatedTvSeries(PAGE)
        }
    }

    companion object {
        private const val PAGE = 1

        private val mockTv1 = TopRatedTvSeries(
            id = 1396,
            name = "Breaking Bad",
            voteAverage = 8.9,
            firstAirDate = "2008-01-20",
            posterUrl = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            genreIds = listOf(18, 80),
        )

        private val mockTv2 = TopRatedTvSeries(
            id = 87108,
            name = "Chernobyl",
            voteAverage = 9.0,
            firstAirDate = "2019-05-06",
            posterUrl = "/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg",
            genreIds = listOf(18, 36),
        )

        val mockTopRatedTvSeries = listOf(mockTv1, mockTv2)
    }
}