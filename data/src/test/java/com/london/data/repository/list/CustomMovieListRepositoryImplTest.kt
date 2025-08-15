package com.london.data.repository.list

import com.google.common.truth.Truth.assertThat
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.customLists.CustomMovieListLocalDataSource
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.list.CustomListResponse
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.remote.model.list.ListDetailsResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.domain.AppPreferencesService
import com.london.domain.language.AppLanguage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class CustomMovieListRepositoryImplTest {

    private lateinit var remoteDataSource: CustomMovieListsRemoteDataSource
    private lateinit var localDataSource: CustomMovieListLocalDataSource
    private lateinit var authPreferences: AuthenticationPreferences
    private lateinit var preferencesService: AppPreferencesService
    private lateinit var repository: CustomMovieListRepositoryImpl
    private lateinit var crashReporter: CrashReporter

    @Before
    fun setUp() {

        remoteDataSource = mockk(relaxed = true)
        authPreferences = mockk(relaxed = true)
        preferencesService = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        crashReporter = mockk(relaxed = true)

        every { authPreferences.getSessionId() } returns "session_123"
        every { preferencesService.appLanguage } returns MutableStateFlow(AppLanguage.ENGLISH)

        repository = CustomMovieListRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            authenticationPreferences = authPreferences,
            preferencesService = preferencesService,
            crashReporter = crashReporter
        )
    }

    @Test
    fun `deleteMovieList should return true when data source returns success`() = runTest {

        // Given
        coEvery { remoteDataSource.delete(any(), any()) } returns Result.success(
            CustomListResponse(
                "",
                1
            )
        )

        // When
        val result = repository.deleteMovieList(1)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `deleteMovieList should return false when data source returns failure`() = runTest {

        // Given
        coEvery { remoteDataSource.delete(any(), any()) } returns Result.failure(Exception("error"))

        // When
        val result = repository.deleteMovieList(1)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `createMovieList should return true when data source returns success`() = runTest {

        // Given
        coEvery { remoteDataSource.create(any(), any(), any()) } returns Result.success(
            mockk(
                relaxed = true
            )
        )

        // When
        val result = repository.createMovieList("My List")

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `createMovieList should return false when data source returns failure`() = runTest {

        // Given
        coEvery {
            remoteDataSource.create(
                any(),
                any(),
                any()
            )
        } returns Result.failure(Exception("error"))

        // When
        val result = repository.createMovieList("My List")

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `getMovieLists should return paged data when data source returns success`() = runTest {

        // Given
        coEvery { remoteDataSource.getAllMovieLists(any(), any()) } returns Result.success(
            MovieListResponseMock
        )

        // When
        val result = repository.getMovieLists(1)

        // Then
        assertThat(result.totalItems).isEqualTo(1)
    }

    @Test
    fun `getMovieLists should throw exception when data source returns failure`() = runTest {

        // Given
        coEvery { remoteDataSource.getAllMovieLists(any(), any()) } returns Result.failure(
            NetworkException.HttpLockedException("locked")
        )

        // When & Then
        assertThrows<NetworkException.HttpLockedException> {
            repository.getMovieLists(1)
        }
    }

    @Test
    fun `addMovieToList should return true when data source returns success`() = runTest {

        // Given
        coEvery { remoteDataSource.addMovieToList(any(), any(), any()) } returns Result.success(
            CustomListResponse("", 1)
        )

        // When
        val result = repository.addMovieToList(1, 100)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `addMovieToList should return false when data source returns failure`() = runTest {

        // Given
        coEvery { remoteDataSource.addMovieToList(any(), any(), any()) } returns Result.failure(
            Exception("error")
        )

        // When
        val result = repository.addMovieToList(1, 100)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `removeMovieFromList should return true when data source returns success`() = runTest {

        // Given
        coEvery {
            remoteDataSource.removeMovieFromList(
                any(),
                any(),
                any()
            )
        } returns Result.success(
            CustomListResponse("", 1)
        )

        // When
        val result = repository.removeMovieFromList(1, 100)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `removeMovieFromList should return false when data source returns failure`() = runTest {

        // Given
        coEvery { remoteDataSource.addMovieToList(any(), any(), any()) } returns Result.failure(
            Exception("error")
        )

        // When
        val result = repository.addMovieToList(1, 100)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `getMovieListDetails should return paged data when data source returns success`() =
        runTest {

            // Given
            coEvery { remoteDataSource.getDetails(any(), any()) } returns Result.success(
                MovieListDetailsMock
            )

            // When
            val result = repository.getMovieListDetails(1, 1)

            // Then
            assertThat(result.totalItems).isEqualTo(1)
        }

    @Test
    fun `getMovieListDetails should throw exception when data source returns failure`() = runTest {

        // Given
        coEvery { remoteDataSource.getDetails(any(), any()) } returns Result.failure(
            NetworkException.HttpLockedException("locked")
        )

        // When & Then
        assertThrows<NetworkException.HttpLockedException> {
            repository.getMovieListDetails(1, 1)
        }
    }

    @Test
    fun `getMovieListName should return name when data source returns movie list details`() =
        runTest {
            // Given
            coEvery { remoteDataSource.getDetails(any(), any()) } returns Result.success(
                MovieListDetailsMock
            )

            // When
            val result = repository.getMovieListName(1)

            // Then
            assertThat(result).isEqualTo(MovieListDetailsMock.name)
        }

    @Test
    fun `getMovieListName should throw exception when data source returns failure`() = runTest {
        // Given
        coEvery { remoteDataSource.getDetails(any(), any()) } returns Result.failure(
            NetworkException.HttpLockedException("locked")
        )

        // When & Then
        assertThrows<NetworkException.HttpLockedException> {
            repository.getMovieListName(1)
        }
    }

    private companion object {
        val MovieListResponseMock = ApiResponse(
            currentPage = 1,
            items = listOf(
                CustomMovieListResponse(
                    description = "desc",
                    favoriteCount = 0,
                    id = 1,
                    itemCount = 1,
                    iso6391 = "en",
                    listType = "movie",
                    name = "My List",
                    posterPath = ""
                )
            ),
            totalPages = 1,
            totalItems = 1
        )
        val MovieListDetailsMock = ListDetailsResponse(
            itemCount = 1,
            items = listOf(
                MovieRemote(
                    adult = false,
                    backdropPath = "",
                    genreIds = emptyList(),
                    id = 100,
                    originalLanguage = "en",
                    originalTitle = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "",
                    releaseDate = "2021-01-01",
                    title = "Test Movie",
                    video = false,
                    voteAverage = 7.5,
                    voteCount = 10,
                    originCountry = listOf(""),
                    originalName = "",
                    firstAirDate = "",
                    name = ""
                )
            ),
            createdBy = "",
            description = "",
            favoriteCount = 0,
            id = "",
            iso6391 = "",
            name = "",
            posterPath = ""
        )
    }
}
