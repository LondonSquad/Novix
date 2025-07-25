package com.london.data.repository.trending

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.remote.home.trending.TrendingRemoteDataSource
import com.london.data.datasource.remote.home.trending.model.TrendingDto
import com.london.data.datasource.remote.home.trending.model.TrendingResponse
import com.london.data.mapper.trending.toActor
import com.london.data.mapper.trending.toTrending
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TrendingRepositoryImplTest {
    private lateinit var remoteDataSource: TrendingRemoteDataSource
    private lateinit var repository: TrendingRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = TrendingRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getTrendingMovies returns expected result`() = runTest {
        coEvery { remoteDataSource.getTrendingMovies(PAGE) } returns trendingResponse
        val result = repository.getTrendingMovies(PAGE)
        assertThat(result.items).isEqualTo(trendingResponse.results.map { it.toTrending() })
    }

    @Test
    fun `getTrendingMovies returns empty result`() = runTest {
        coEvery { remoteDataSource.getTrendingMovies(PAGE) } returns trendingResponseEmpty
        val result = repository.getTrendingMovies(PAGE)
        assertThat(result.items).isEmpty()
    }

    @Test
    fun `getTrendingMovies throws exception on failure`() = runTest {
        coEvery { remoteDataSource.getTrendingMovies(PAGE) } throws RuntimeException("Network error")
        assertThrows<RuntimeException> { repository.getTrendingMovies(PAGE) }
    }

    @Test
    fun `getTrendingTvShows returns expected result`() = runTest {
        coEvery { remoteDataSource.getTrendingTvShows(PAGE) } returns trendingResponse
        val result = repository.getTrendingTvShows(PAGE)
        assertThat(result.items).isEqualTo(trendingResponse.results.map { it.toTrending() })
    }

    @Test
    fun `getTrendingTvShows returns empty result`() = runTest {
        coEvery { remoteDataSource.getTrendingTvShows(PAGE) } returns trendingResponseEmpty
        val result = repository.getTrendingTvShows(PAGE)
        assertThat(result.items).isEmpty()
    }

    @Test
    fun `getTrendingTvShows throws exception on failure`() = runTest {
        coEvery { remoteDataSource.getTrendingTvShows(PAGE) } throws RuntimeException("Network error")
        assertThrows<RuntimeException> { repository.getTrendingTvShows(PAGE) }
    }

    @Test
    fun `getTrendingActors returns expected result`() = runTest {
        coEvery { remoteDataSource.getTrendingActors(PAGE) } returns trendingResponse
        val result = repository.getTrendingActors(PAGE)
        assertThat(result.items).isEqualTo(trendingResponse.results.map { it.toActor() })
    }

    @Test
    fun `getTrendingActors returns empty result`() = runTest {
        coEvery { remoteDataSource.getTrendingActors(PAGE) } returns trendingResponseEmpty
        val result = repository.getTrendingActors(PAGE)
        assertThat(result.items).isEmpty()
    }

    @Test
    fun `getTrendingActors throws exception on failure`() = runTest {
        coEvery { remoteDataSource.getTrendingActors(PAGE) } throws RuntimeException("Network error")
        assertThrows<RuntimeException> { repository.getTrendingActors(PAGE) }
    }

    private companion object {
        const val PAGE = 1
        val trendingDto = TrendingDto(
            id = 1,
            title = "Test",
            name = "Test",
            posterPath = "",
            profilePath = "",
            genreIds = listOf(1, 2, 3)
        )
        val trendingResponse = TrendingResponse(
            results = listOf(trendingDto),
            page = 1,
            totalPages = 1
        )
        val trendingResponseEmpty = TrendingResponse(
            results = emptyList(),
            page = 1,
            totalPages = 1
        )
    }
} 