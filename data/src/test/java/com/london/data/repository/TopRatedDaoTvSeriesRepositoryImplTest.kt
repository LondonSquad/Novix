package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.home.TopRatedLocal
import com.london.data.local.source.home.popular.HomeLocalDataSource
import com.london.data.mapper.toprated.toEntity
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.TopRatedTvSeriesRemote
import com.london.data.remote.source.toprated.tvseries.TopRatedTvRemoteDataSource
import com.london.data.repository.toprated.TopRatedTvSeriesRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedTvSeries
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TopRatedTvSeriesRepositoryImplTest {

    private lateinit var remoteDataSource: TopRatedTvRemoteDataSource
    private lateinit var localDataSource: HomeLocalDataSource<TopRatedLocal>
    private lateinit var crashReporter: CrashReporter
    private lateinit var repository: TopRatedTvSeriesRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        crashReporter = mockk(relaxed = true)
        repository = TopRatedTvSeriesRepositoryImpl(
            topRatedTvRemoteDataSource = remoteDataSource,
            topRatedTvShow = localDataSource,
            crashReporter = crashReporter
        )
    }

    @Test
    fun `getTopRatedTvSeries should return paged response with correct data`() = runTest {
        // Given
        val expectedApiResponse = fakeApiResponseWithTvSeries()
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE)
        } returns Result.success(expectedApiResponse)

        coEvery {
            localDataSource.getAll()
        } returns emptyList()

        // When
        val result: PagedFetchResponse<TopRatedTvSeries> = repository.getTopRatedTvSeries(PAGE)

        // Then
        assertThat(result.items).hasSize(2)
        assertThat(result.currentPage).isEqualTo(PAGE)
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.totalItems).isEqualTo(2)

        val firstSeries = result.items.first()
        assertThat(firstSeries).isEqualTo(
            expectedApiResponse.items[0].toEntity()
        )

        val secondSeries = result.items[1]
        assertThat(secondSeries).isEqualTo(
            expectedApiResponse.items[1].toEntity()
        )
    }

    @Test
    fun `getTopRatedTvSeries should return empty paged response when API returns empty results`() = runTest {
        // Given
        val emptyApiResponse = fakeEmptyApiResponse()
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE)
        } returns Result.success(emptyApiResponse)

        coEvery {
            localDataSource.getAll()
        } returns emptyList()

        // When
        val result = repository.getTopRatedTvSeries(PAGE)

        // Then
        assertThat(result.items).isEmpty()
        assertThat(result.currentPage).isEqualTo(PAGE)
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.totalItems).isEqualTo(0)
    }

    @Test
    fun `getTopRatedTvSeries should propagate exceptions when remote call fails`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE)
        } returns Result.failure(RuntimeException("Network error"))

        coEvery {
            localDataSource.getAll()
        } returns emptyList()

        // When & Then
        val ex = assertThrows<RuntimeException> {
            repository.getTopRatedTvSeries(PAGE)
        }
        assertThat(ex.message).isEqualTo("Network error")
    }

    @Test
    fun `getTopRatedTvSeries should call local data source for caching`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE)
        } returns Result.success(fakeApiResponseWithTvSeries())

        coEvery {
            localDataSource.getAll()
        } returns emptyList()

        // When
        repository.getTopRatedTvSeries(PAGE)

        // Then
        coVerify { localDataSource.getAll() }
    }

    @Test
    fun `getTopRatedTvSeries should insert data to local storage after successful fetch`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedTvShows(PAGE)
        } returns Result.success(fakeApiResponseWithTvSeries())

        coEvery {
            localDataSource.getAll()
        } returns emptyList()

        coEvery {
            localDataSource.insertAll(any())
        } returns Unit

        // When
        repository.getTopRatedTvSeries(PAGE)

        // Then
        coVerify { localDataSource.insertAll(any()) }
    }

    companion object {
        private const val PAGE = 1

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