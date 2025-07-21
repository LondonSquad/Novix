package com.london.data.datasource.remote.home.popular

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.api.PopularApiService
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PopularRemoteDataSourceImplTest {

    private lateinit var apiService: PopularApiService
    private lateinit var remoteDataSource: PopularRemoteDataSourceImpl

    @Before
    fun setup() {
        apiService = mockk()
        remoteDataSource = PopularRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getPopularMovies returns expected data`() = runTest {
        // Given
        val fakeMovie = PopularMovieResponse(
            adult = false,
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(28, 12),
            id = 1,
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "A fake movie.",
            popularity = 100.0,
            posterPath = "/poster.jpg",
            releaseDate = "2025-01-01",
            title = "Fake Movie",
            video = false,
            voteAverage = 8.5,
            voteCount = 2000
        )

        val expectedResponse = ApiResponse(
            currentPage = 1,
            items = listOf(fakeMovie),
            totalPages = 10,
            totalItems = 100
        )

        coEvery { apiService.getPopularMovies() } returns expectedResponse

        // When
        val result = remoteDataSource.getPopularMovies()

        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { apiService.getPopularMovies() }
    }
}
