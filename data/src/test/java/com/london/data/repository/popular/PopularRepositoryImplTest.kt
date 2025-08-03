package com.london.data.repository.popular

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.source.HomeLocalDataSource
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.popular.PopularMovieResponse
import com.london.data.remote.model.home.model.popular.PopularTvShowResponse
import com.london.data.remote.source.home.popular.PopularRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.domain.entity.recent.MediaType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class PopularRepositoryImplTest {

    private val remoteDataSource: PopularRemoteDataSource = mockk()
    private val homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal> = mockk()
    private val crashReporter: CrashReporter = mockk(relaxed = true)

    private lateinit var repository: PopularRepositoryImpl

    @Before
    fun setUp() {
        repository = PopularRepositoryImpl(
            remoteDataSource,
            homeLocalDataSource,
            crashReporter
        )
    }

    // ==================== Popular Movies Tests ====================

    @Test
    fun `getPopularMovies - when cache is empty should fetch from network and sync to cache`() = runTest {
        // Given
        val capturedItems = slot<List<PopularSectionLocal>>()
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(singleMovieResponse)
        coEvery { homeLocalDataSource.insertAll(capture(capturedItems)) } returns Unit

        // When
        val result = repository.getPopularMovies()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(101)
        assertThat(result[0].title).isEqualTo("Test Movie")

        coVerify(exactly = 1) { homeLocalDataSource.getAll() }
        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() }
        coVerify(exactly = 1) { homeLocalDataSource.insertAll(any()) }

        // Verify sync data
        assertThat(capturedItems.captured).hasSize(1)
        assertThat(capturedItems.captured[0].mediaType).isEqualTo(MediaType.Movie)
        assertThat(capturedItems.captured[0].id).isEqualTo(101)
    }

    @Test
    fun `getPopularMovies - when cache has data should return cached data without network call`() = runTest {
        // Given
        val cachedData = listOf(
            PopularSectionLocal(
                id = 101,
                name = "Cached Movie",
                posterPictureUrl = "/cached_poster.jpg",
                rating = 8.0,
                mediaType = MediaType.Movie
            )
        )
        coEvery { homeLocalDataSource.getAll() } returns cachedData

        // When
        val result = repository.getPopularMovies()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(101)
        assertThat(result[0].title).isEqualTo("Cached Movie")

        coVerify(exactly = 1) { homeLocalDataSource.getAll() }
        coVerify(exactly = 0) { remoteDataSource.getPopularMovies() }
        coVerify(exactly = 0) { homeLocalDataSource.insertAll(any()) }
    }

    @Test
    fun `getPopularMovies - when cache has mixed media types should filter only movies`() = runTest {
        // Given
        val mixedCachedData = listOf(
            PopularSectionLocal(
                id = 101,
                name = "Movie",
                posterPictureUrl = "/movie_poster.jpg",
                rating = 8.0,
                mediaType = MediaType.Movie
            ),
            PopularSectionLocal(
                id = 201,
                name = "TV Show",
                posterPictureUrl = "/tv_poster.jpg",
                rating = 9.0,
                mediaType = MediaType.TvShow
            )
        )
        coEvery { homeLocalDataSource.getAll() } returns mixedCachedData

        // When
        val result = repository.getPopularMovies()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(101)
        assertThat(result[0].title).isEqualTo("Movie")

        coVerify(exactly = 0) { remoteDataSource.getPopularMovies() }
    }

    @Test
    fun `getPopularMovies - when network returns empty list should return empty list and sync empty data`() = runTest {
        // Given
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(emptyMovieResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val result = repository.getPopularMovies()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { homeLocalDataSource.insertAll(emptyList()) }
    }

    @Test
    fun `getPopularMovies - when network returns multiple items should map and sync all items`() = runTest {
        // Given
        val capturedItems = slot<List<PopularSectionLocal>>()
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(multipleMoviesResponse)
        coEvery { homeLocalDataSource.insertAll(capture(capturedItems)) } returns Unit

        // When
        val result = repository.getPopularMovies()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Test Movie 1")
        assertThat(result[1].title).isEqualTo("Test Movie 2")

        // Verify all items were synced
        assertThat(capturedItems.captured).hasSize(2)
        assertThat(capturedItems.captured.all { it.mediaType == MediaType.Movie }).isTrue()
    }

    @Test
    fun `getPopularMovies - when network throws UnAuthorizedException should propagate exception and log crash`() = runTest {
        // Given
        val exception = NetworkException.UnAuthorizedException("401 Unauthorized")
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularMovies() } throws exception

        // When & Then
        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getPopularMovies()
        }

        verify { crashReporter.logException(exception) }
        coVerify(exactly = 0) { homeLocalDataSource.insertAll(any()) }
    }

    @Test
    fun `getPopularMovies - when network throws TimeoutException should propagate exception and log crash`() = runTest {
        // Given
        val exception = NetworkException.TimeoutException("Request timed out")
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularMovies() } throws exception

        // When & Then
        assertThrows<NetworkException.TimeoutException> {
            repository.getPopularMovies()
        }

        verify { crashReporter.logException(exception) }
    }

    @Test
    fun `getPopularMovies - when network returns failure result should throw exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularMovies() } returns Result.failure(exception)

        // When & Then
        assertThrows<RuntimeException> {
            repository.getPopularMovies()
        }
    }

    // ==================== Popular TV Shows Tests ====================

    @Test
    fun `getPopularTvShows - when cache is empty should fetch from network and sync to cache`() = runTest {
        // Given
        val capturedItems = slot<List<PopularSectionLocal>>()
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(singleTvShowResponse)
        coEvery { homeLocalDataSource.insertAll(capture(capturedItems)) } returns Unit

        // When
        val result = repository.getPopularTvShows()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(201)
        assertThat(result[0].name).isEqualTo("Test TV Show")

        coVerify(exactly = 1) { homeLocalDataSource.getAll() }
        coVerify(exactly = 1) { remoteDataSource.getPopularTvShows() }
        coVerify(exactly = 1) { homeLocalDataSource.insertAll(any()) }

        // Verify sync data
        assertThat(capturedItems.captured).hasSize(1)
        assertThat(capturedItems.captured[0].mediaType).isEqualTo(MediaType.TvShow)
        assertThat(capturedItems.captured[0].id).isEqualTo(201)
    }

    @Test
    fun `getPopularTvShows - when cache has data should return cached data without network call`() = runTest {
        // Given
        val cachedData = listOf(
            PopularSectionLocal(
                id = 201,
                name = "Cached TV Show",
                posterPictureUrl = "/cached_tv_poster.jpg",
                rating = 9.5,
                mediaType = MediaType.TvShow
            )
        )
        coEvery { homeLocalDataSource.getAll() } returns cachedData

        // When
        val result = repository.getPopularTvShows()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(201)
        assertThat(result[0].name).isEqualTo("Cached TV Show")

        coVerify(exactly = 1) { homeLocalDataSource.getAll() }
        coVerify(exactly = 0) { remoteDataSource.getPopularTvShows() }
        coVerify(exactly = 0) { homeLocalDataSource.insertAll(any()) }
    }

    @Test
    fun `getPopularTvShows - when cache has mixed media types should filter only tv shows`() = runTest {
        // Given
        val mixedCachedData = listOf(
            PopularSectionLocal(
                id = 101,
                name = "Movie",
                posterPictureUrl = "/movie_poster.jpg",
                rating = 8.0,
                mediaType = MediaType.Movie
            ),
            PopularSectionLocal(
                id = 201,
                name = "TV Show",
                posterPictureUrl = "/tv_poster.jpg",
                rating = 9.0,
                mediaType = MediaType.TvShow
            )
        )
        coEvery { homeLocalDataSource.getAll() } returns mixedCachedData

        // When
        val result = repository.getPopularTvShows()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(201)
        assertThat(result[0].name).isEqualTo("TV Show")

        coVerify(exactly = 0) { remoteDataSource.getPopularTvShows() }
    }

    @Test
    fun `getPopularTvShows - when network returns empty list should return empty list and sync empty data`() = runTest {
        // Given
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(emptyTvShowResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val result = repository.getPopularTvShows()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { homeLocalDataSource.insertAll(emptyList()) }
    }

    @Test
    fun `getPopularTvShows - when network returns multiple items should map and sync all items`() = runTest {
        // Given
        val capturedItems = slot<List<PopularSectionLocal>>()
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(multipleTvShowsResponse)
        coEvery { homeLocalDataSource.insertAll(capture(capturedItems)) } returns Unit

        // When
        val result = repository.getPopularTvShows()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Test TV Show 1")
        assertThat(result[1].name).isEqualTo("Test TV Show 2")

        // Verify all items were synced
        assertThat(capturedItems.captured).hasSize(2)
        assertThat(capturedItems.captured.all { it.mediaType == MediaType.TvShow }).isTrue()
    }

    @Test
    fun `getPopularTvShows - when network throws HttpLockedException should propagate exception and log crash`() = runTest {
        // Given
        val exception = NetworkException.HttpLockedException("Resource locked")
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularTvShows() } throws exception

        // When & Then
        assertThrows<NetworkException.HttpLockedException> {
            repository.getPopularTvShows()
        }

        verify { crashReporter.logException(exception) }
        coVerify(exactly = 0) { homeLocalDataSource.insertAll(any()) }
    }

    @Test
    fun `getPopularTvShows - when network throws ValidationException should propagate exception and log crash`() = runTest {
        // Given
        val exception = NetworkException.ValidationException("Invalid data")
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularTvShows() } throws exception

        // When & Then
        assertThrows<NetworkException.ValidationException> {
            repository.getPopularTvShows()
        }

        verify { crashReporter.logException(exception) }
    }

    @Test
    fun `getPopularTvShows - when network returns failure result should throw exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.failure(exception)

        // When & Then
        assertThrows<RuntimeException> {
            repository.getPopularTvShows()
        }
    }

    // ==================== Cache Behavior Tests ====================

    @Test
    fun `getPopularMovies - when cache has only tv shows should fetch from network`() = runTest {
        // Given
        val tvShowCacheData = listOf(
            PopularSectionLocal(
                id = 201,
                name = "TV Show Only",
                posterPictureUrl = "/tv_poster.jpg",
                rating = 9.0,
                mediaType = MediaType.TvShow
            )
        )
        coEvery { homeLocalDataSource.getAll() } returns tvShowCacheData
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(singleMovieResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val result = repository.getPopularMovies()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Test Movie")

        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() }
    }

    @Test
    fun `getPopularTvShows - when cache has only movies should fetch from network`() = runTest {
        // Given
        val movieCacheData = listOf(
            PopularSectionLocal(
                id = 101,
                name = "Movie Only",
                posterPictureUrl = "/movie_poster.jpg",
                rating = 8.0,
                mediaType = MediaType.Movie
            )
        )
        coEvery { homeLocalDataSource.getAll() } returns movieCacheData
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(singleTvShowResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val result = repository.getPopularTvShows()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Test TV Show")

        coVerify(exactly = 1) { remoteDataSource.getPopularTvShows() }
    }

    // ==================== Data Mapping Verification Tests ====================

    @Test
    fun `getPopularMovies - should correctly map all movie fields from network response`() = runTest {
        // Given
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(singleMovieResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val result = repository.getPopularMovies()

        // Then
        val movie = result[0]
        assertThat(movie.id).isEqualTo(101)
        assertThat(movie.title).isEqualTo("Test Movie")
        assertThat(movie.posterUrl).contains("/poster.jpg")
        assertThat(movie.rating).isEqualTo(7.8)
    }

    @Test
    fun `getPopularTvShows - should correctly map all tv show fields from network response`() = runTest {
        // Given
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(singleTvShowResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val result = repository.getPopularTvShows()

        // Then
        val tvShow = result[0]
        assertThat(tvShow.id).isEqualTo(201)
        assertThat(tvShow.name).isEqualTo("Test TV Show")
        assertThat(tvShow.posterUrl).contains("/tv_poster.jpg")
        assertThat(tvShow.rating).isEqualTo(8.5)
    }

    // ==================== Integration Tests ====================

    @Test
    fun `getPopularMovies - end to end flow with successful caching and retrieval`() = runTest {
        // Setup: First call with empty cache
        coEvery { homeLocalDataSource.getAll() } returns emptyList() andThen listOf(
            PopularSectionLocal(
                id = 101,
                name = "Test Movie",
                posterPictureUrl = "/poster.jpg",
                rating = 7.8,
                mediaType = MediaType.Movie
            )
        )
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(singleMovieResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // First call - should fetch from network
        val firstResult = repository.getPopularMovies()
        assertThat(firstResult).hasSize(1)
        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() }

        // Second call - should use cache
        val secondResult = repository.getPopularMovies()
        assertThat(secondResult).hasSize(1)
        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() } // Still only one call
    }

    @Test
    fun `concurrent calls to different media types should work independently`() = runTest {
        // Given
        coEvery { homeLocalDataSource.getAll() } returns emptyList()
        coEvery { remoteDataSource.getPopularMovies() } returns Result.success(singleMovieResponse)
        coEvery { remoteDataSource.getPopularTvShows() } returns Result.success(singleTvShowResponse)
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val movies = repository.getPopularMovies()
        val tvShows = repository.getPopularTvShows()

        // Then
        assertThat(movies).hasSize(1)
        assertThat(tvShows).hasSize(1)
        assertThat(movies[0].title).isEqualTo("Test Movie")
        assertThat(tvShows[0].name).isEqualTo("Test TV Show")

        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() }
        coVerify(exactly = 1) { remoteDataSource.getPopularTvShows() }
    }

    // ==================== Test Data ====================

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