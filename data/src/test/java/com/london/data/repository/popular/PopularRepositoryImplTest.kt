package com.london.data.repository.popular

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.PopularRemoteDataSource
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import com.london.domain.entity.popular.PopularMovie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PopularRepositoryImplTest {

    private val remoteDataSource: PopularRemoteDataSource = mockk()
    private lateinit var repository: PopularRepositoryImpl

    @Before
    fun setUp() {
        repository = PopularRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `when call getPopularMovies should returns mapped domain models`() = runTest {
        // Given
        val remoteResponse = ApiResponse(
            currentPage = 1,
            totalItems = 100,
            totalPages = 200,
            items = listOf(
                PopularMovieResponse(
                    adult = false,
                    backdropPath = "/backdrop.jpg",
                    genreIds = listOf(1, 2),
                    id = 101,
                    originalLanguage = "en",
                    originalTitle = "Original Title",
                    overview = "Some overview",
                    popularity = 100.0,
                    posterPath = "/poster.jpg",
                    releaseDate = "2024-01-01",
                    title = "Test Movie",
                    video = false,
                    voteAverage = 7.8,
                    voteCount = 2000
                )
            )
        )
        coEvery { remoteDataSource.getPopularMovies() } returns remoteResponse

        // When
        val result: List<PopularMovie> = repository.getPopularMovies()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(101)
        assertThat(result[0].title).isEqualTo("Test Movie")
        assertThat(result[0].posterUrl).contains("/poster.jpg")
        assertThat(result[0].rating).isEqualTo(7.8)

        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() }
    }
}
