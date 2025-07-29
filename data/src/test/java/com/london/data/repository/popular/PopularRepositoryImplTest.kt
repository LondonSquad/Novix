package com.london.data.repository.popular

import com.google.common.truth.Truth.assertThat
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.source.home.popular.PopularRemoteDataSource
import com.london.data.remote.model.home.model.popular.PopularMovieResponse
import com.london.data.remote.model.home.model.popular.PopularTvShowResponse
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.entity.popular.PopularTvShow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

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
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(singleMovieResponse)

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

    @Test
    fun `when call getPopularMovies with empty list should return empty domain list`() = runTest {
        // Given
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(emptyMovieResponse)

        // When
        val result: List<PopularMovie> = repository.getPopularMovies()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() }
    }

    @Test
    fun `when call getPopularMovies with multiple items should return mapped list`() = runTest {
        // Given
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(
            multipleMoviesResponse
        )

        // When
        val result: List<PopularMovie> = repository.getPopularMovies()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(101)
        assertThat(result[0].title).isEqualTo("Test Movie 1")
        assertThat(result[1].id).isEqualTo(102)
        assertThat(result[1].title).isEqualTo("Test Movie 2")

        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() }
    }

    @Test
    fun `when call getPopularTvShows should returns mapped domain models`() = runTest {
        // Given
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(singleTvShowResponse)

        // When
        val result: List<PopularTvShow> = repository.getPopularTvShows()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(201)
        assertThat(result[0].name).isEqualTo("Test TV Show")
        assertThat(result[0].posterUrl).contains("/tv_poster.jpg")
        assertThat(result[0].rating).isEqualTo(8.5)

        coVerify(exactly = 1) { remoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `when call getPopularTvShows with empty list should return empty domain list`() = runTest {
        // Given
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(emptyTvShowResponse)

        // When
        val result: List<PopularTvShow> = repository.getPopularTvShows()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { remoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `when call getPopularTvShows with multiple items should return mapped list`() = runTest {
        // Given
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(
            multipleTvShowsResponse
        )

        // When
        val result: List<PopularTvShow> = repository.getPopularTvShows()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(201)
        assertThat(result[0].name).isEqualTo("Test TV Show 1")
        assertThat(result[1].id).isEqualTo(202)
        assertThat(result[1].name).isEqualTo("Test TV Show 2")

        coVerify(exactly = 1) { remoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `getPopularMovies should throw UnAuthorizedException when API returns 401`() = runTest {
        coEvery { remoteDataSource.getPopularMovies() } throws
                NetworkException.UnAuthorizedException("401 Unauthorized")

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getPopularMovies()
        }
    }

    @Test
    fun `getPopularMovies should throw TimeoutException when API times out`() = runTest {
        coEvery { remoteDataSource.getPopularMovies() } throws NetworkException.TimeoutException("Request timed out")

        assertThrows<NetworkException.TimeoutException> {
            repository.getPopularMovies()
        }
    }

    @Test
    fun `getPopularTvShows should throw HttpLockedException when API returns 423`() = runTest {
        coEvery { remoteDataSource.getPopularTvShows() } throws NetworkException.HttpLockedException(
            "Resource locked"
        )

        assertThrows<NetworkException.HttpLockedException> {
            repository.getPopularTvShows()
        }
    }

    @Test
    fun `getPopularTvShows should throw ValidationException when API returns 422`() = runTest {
        coEvery { remoteDataSource.getPopularTvShows() } throws NetworkException.ValidationException(
            "Invalid data"
        )

        assertThrows<NetworkException.ValidationException> {
            repository.getPopularTvShows()
        }
    }


    companion object TestData {
        val singleMovieResponse = ApiResponse(
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

        val emptyMovieResponse = ApiResponse<PopularMovieResponse>(
            currentPage = 1,
            totalItems = 0,
            totalPages = 0,
            items = emptyList()
        )

        val multipleMoviesResponse = ApiResponse(
            currentPage = 1,
            totalItems = 2,
            totalPages = 1,
            items = listOf(
                PopularMovieResponse(
                    adult = false,
                    backdropPath = "/backdrop1.jpg",
                    genreIds = listOf(1, 2),
                    id = 101,
                    originalLanguage = "en",
                    originalTitle = "Original Title 1",
                    overview = "Overview 1",
                    popularity = 100.0,
                    posterPath = "/poster1.jpg",
                    releaseDate = "2024-01-01",
                    title = "Test Movie 1",
                    video = false,
                    voteAverage = 7.8,
                    voteCount = 2000
                ),
                PopularMovieResponse(
                    adult = false,
                    backdropPath = "/backdrop2.jpg",
                    genreIds = listOf(3, 4),
                    id = 102,
                    originalLanguage = "fr",
                    originalTitle = "Original Title 2",
                    overview = "Overview 2",
                    popularity = 90.0,
                    posterPath = "/poster2.jpg",
                    releaseDate = "2024-02-01",
                    title = "Test Movie 2",
                    video = false,
                    voteAverage = 8.2,
                    voteCount = 1500
                )
            )
        )

        val singleTvShowResponse = ApiResponse(
            currentPage = 1,
            totalItems = 50,
            totalPages = 100,
            items = listOf(
                PopularTvShowResponse(
                    adult = false,
                    backdropPath = "/tv_backdrop.jpg",
                    genreIds = listOf(5, 6),
                    id = 201,
                    originCountry = listOf("US"),
                    originalLanguage = "en",
                    originalName = "Original TV Show",
                    overview = "TV Show overview",
                    popularity = 85.0,
                    posterPath = "/tv_poster.jpg",
                    firstAirDate = "2024-03-01",
                    name = "Test TV Show",
                    voteAverage = 8.5,
                    voteCount = 1200
                )
            )
        )

        val emptyTvShowResponse = ApiResponse<PopularTvShowResponse>(
            currentPage = 1,
            totalItems = 0,
            totalPages = 0,
            items = emptyList()
        )

        val multipleTvShowsResponse = ApiResponse(
            currentPage = 1,
            totalItems = 2,
            totalPages = 1,
            items = listOf(
                PopularTvShowResponse(
                    adult = false,
                    backdropPath = "/tv_backdrop1.jpg",
                    genreIds = listOf(5, 6),
                    id = 201,
                    originCountry = listOf("US"),
                    originalLanguage = "en",
                    originalName = "Original TV Show 1",
                    overview = "TV Show overview 1",
                    popularity = 85.0,
                    posterPath = "/tv_poster1.jpg",
                    firstAirDate = "2024-03-01",
                    name = "Test TV Show 1",
                    voteAverage = 8.5,
                    voteCount = 1200
                ),
                PopularTvShowResponse(
                    adult = false,
                    backdropPath = "/tv_backdrop2.jpg",
                    genreIds = listOf(7, 8),
                    id = 202,
                    originCountry = listOf("UK"),
                    originalLanguage = "en",
                    originalName = "Original TV Show 2",
                    overview = "TV Show overview 2",
                    popularity = 75.0,
                    posterPath = "/tv_poster2.jpg",
                    firstAirDate = "2024-04-01",
                    name = "Test TV Show 2",
                    voteAverage = 9.0,
                    voteCount = 800
                )
            )
        )
    }
}