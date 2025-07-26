package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.source.toprated.tvseries.TopRatedTvRemoteDataSource
import com.london.data.remote.model.toprated.tvshow.model.TopRatedTvSeriesRemote
import com.london.data.mapper.toprated.toEntity
import com.london.domain.entity.toprated.TopRatedTvSeries
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TopRatedTvSeriesRepositoryImplTest {

    private lateinit var remoteDataSource: TopRatedTvRemoteDataSource
    private lateinit var repository: TopRatedTvSeriesRepoImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = TopRatedTvSeriesRepoImpl(remoteDataSource)
    }

    @Test
    fun `getTopRatedTvSeries should map remote series list correctly`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE, LANGUAGE)
        } returns fakeApiResponseWithTvSeries()

        // When
        val result: List<TopRatedTvSeries> = repository.getTopRatedTvSeries(PAGE, LANGUAGE)

        // Then
        assertThat(result).hasSize(2)

        val firstSeries = result.first()
        assertThat(firstSeries).isEqualTo(
            fakeApiResponseWithTvSeries().items[0].toEntity()
        )

        val secondSeries = result[1]
        assertThat(secondSeries).isEqualTo(
            fakeApiResponseWithTvSeries().items[1].toEntity()
        )
    }

    @Test
    fun `getTopRatedTvSeries should return empty list when API returns empty results`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE, LANGUAGE)
        } returns fakeEmptyApiResponse()

        // When
        val result = repository.getTopRatedTvSeries(PAGE, LANGUAGE)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTopRatedTvSeries should propagate exceptions`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE, LANGUAGE)
        } throws RuntimeException("Network error")

        // When && Then
        val ex = assertThrows<RuntimeException> {
            repository.getTopRatedTvSeries(PAGE, LANGUAGE)
        }
        assertThat(ex.message).isEqualTo("Network error")
    }

    companion object {
        private const val PAGE = 1
        private const val LANGUAGE = "en-US"

        private fun fakeApiResponseWithTvSeries() = ApiResponse(
            currentPage = PAGE,
            totalPages = 1,
            totalItems = 2,
            items = listOf(
                TopRatedTvSeriesRemote(
                    adult = false,
                    backdropPath = "/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
                    genreIds = listOf(18, 80),
                    id = 1396,
                    originalLanguage = "en",
                    originalName = "Breaking Bad",
                    overview = "A chemistry teacher diagnosed with cancer starts manufacturing meth.",
                    popularity = 100.0,
                    posterPath = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
                    firstAirDate = "2008-01-20",
                    name = "Breaking Bad",
                    originCountry = listOf("US"),
                    voteAverage = 8.9,
                    voteCount = 18000
                ),
                TopRatedTvSeriesRemote(
                    adult = false,
                    backdropPath = "/scZlQQYnDVlnpxFTxaIv2g0BWnL.jpg",
                    genreIds = listOf(18, 36),
                    id = 87108,
                    originalLanguage = "en",
                    originalName = "Chernobyl",
                    overview = "A dramatization of the true story of the Chernobyl disaster.",
                    popularity = 75.5,
                    posterPath = "/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg",
                    firstAirDate = "2019-05-06",
                    name = "Chernobyl",
                    originCountry = listOf("US", "GB"),
                    voteAverage = 9.0,
                    voteCount = 12000
                )
            )
        )

        private fun fakeEmptyApiResponse() = ApiResponse(
            currentPage = PAGE,
            totalPages = 1,
            totalItems = 0,
            items = emptyList<TopRatedTvSeriesRemote>()
        )
    }
}
