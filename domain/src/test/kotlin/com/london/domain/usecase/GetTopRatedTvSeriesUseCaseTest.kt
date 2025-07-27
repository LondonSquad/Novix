package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.TopRatedTvSeriesRepository
import com.london.domain.entity.PagedFetchResponse
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
            originalName = "Breaking Bad",
            overview = "A chemistry teacher diagnosed with cancer starts manufacturing meth.",
            popularity = 100.0,
            voteAverage = 8.9,
            voteCount = 18000,
            firstAirDate = "2008-01-20",
            backdropUrl = "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
            posterUrl = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            genreIds = listOf(18, 80),
            originCountry = listOf("US"),
            originalLanguage = "en",
            adult = false
        )

        private val mockTv2 = TopRatedTvSeries(
            id = 87108,
            name = "Chernobyl",
            originalName = "Chernobyl",
            overview = "A dramatization of the true story of the Chernobyl disaster.",
            popularity = 75.5,
            voteAverage = 9.0,
            voteCount = 12000,
            firstAirDate = "2019-05-06",
            backdropUrl = "/scZlQQYnDVlnpxFTxaIv2g0BWnL.jpg",
            posterUrl = "/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg",
            genreIds = listOf(18, 36),
            originCountry = listOf("US", "GB"),
            originalLanguage = "en",
            adult = false
        )

        val mockTopRatedTvSeries = listOf(mockTv1, mockTv2)
    }
}
