package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.TopRatedTvSeriesRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedTvSeriesUseCaseTest {

    private lateinit var repository: TopRatedTvSeriesRepo
    private lateinit var getTopRatedTvSeries: GetTopRatedTvSeriesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getTopRatedTvSeries = GetTopRatedTvSeriesUseCase(repository)
    }

    @Test
    fun `should return TV series when repository returns valid list`() = runTest {
        // Given
        coEvery {
            repository.getTopRatedTvSeries(PAGE, LANGUAGE)
        } returns mockTopRatedTvSeries

        // When
        val result = getTopRatedTvSeries(PAGE, LANGUAGE)

        // Then
        assertThat(result).isEqualTo(mockTopRatedTvSeries)
        assertThat(result).hasSize(2)
    }

    @Test
    fun `should return empty list when repository returns empty list`() = runTest {
        // Given
        coEvery {
            repository.getTopRatedTvSeries(PAGE, LANGUAGE)
        } returns emptyList()

        // When
        val result = getTopRatedTvSeries(PAGE, LANGUAGE)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw RuntimeException when repository throws`() = runTest {
        // Given
        coEvery {
            repository.getTopRatedTvSeries(PAGE, LANGUAGE)
        } throws RuntimeException("Something went wrong")

        // When & Then
        assertThrows<RuntimeException> {
            getTopRatedTvSeries(PAGE, LANGUAGE)
        }
    }

    companion object {
        private const val PAGE = 1
        private const val LANGUAGE = "en-US"

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
