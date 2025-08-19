package com.london.data.repository.list

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.customLists.MovieListLocal
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.customLists.CustomMovieListLocalDataSource
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.remote.model.list.ListDetailsResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.domain.AppPreferencesService
import com.london.domain.entity.language.AppLanguage
import io.mockk.coEvery
import io.mockk.coVerify
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
        localDataSource = mockk(relaxed = true)
        authPreferences = mockk(relaxed = true)
        preferencesService = mockk(relaxed = true)
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
    fun `deleteMovieList should return true and clear cache on remote success`() = runTest {
        // Given
        coEvery { remoteDataSource.delete(any(), any()) } returns Result.success(mockk())
        val listId = 1

        // When
        val result = repository.deleteMovieList(listId)

        // Then
        assertThat(result).isTrue()
        coVerify { remoteDataSource.delete(listId, "session_123") }
        coVerify { localDataSource.removeMovieListCache(listId) }
    }

    @Test
    fun `deleteMovieList should return false and not touch cache on remote failure`() = runTest {
        // Given
        coEvery { remoteDataSource.delete(any(), any()) } returns Result.failure(Exception("error"))
        val listId = 1

        // When
        val result = repository.deleteMovieList(listId)

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 0) { localDataSource.removeMovieListCache(any()) }
    }

    @Test
    fun `createMovieList should return false and not touch cache on remote failure`() = runTest {
        // Given
        coEvery { remoteDataSource.create(any(), any(), any()) } returns Result.failure(Exception("error"))

        // When
        val result = repository.createMovieList("My List")

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 0) { localDataSource.markCacheRefreshed(any()) }
    }

    @Test
    fun `addMovieToList should return true and update cache on remote success`() = runTest {
        // Given
        coEvery { remoteDataSource.addMovieToList(any(), any(), any()) } returns Result.success(mockk())
        val listId = 1
        val movieId = 100

        // When
        val result = repository.addMovieToList(listId, movieId)

        // Then
        assertThat(result).isTrue()
        coVerify { localDataSource.addMovieToListCache(movieId, listId) }
    }

    @Test
    fun `addMovieToList should return false and not touch cache on remote failure`() = runTest {
        // Given
        coEvery { remoteDataSource.addMovieToList(any(), any(), any()) } returns Result.failure(Exception("error"))

        // When
        val result = repository.addMovieToList(1, 100)

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 0) { localDataSource.addMovieToListCache(any(), any()) }
    }

    @Test
    fun `removeMovieFromList should return true and update cache on remote success`() = runTest {
        // Given
        coEvery { remoteDataSource.removeMovieFromList(any(), any(), any()) } returns Result.success(mockk())
        val listId = 1
        val movieId = 100

        // When
        val result = repository.removeMovieFromList(listId, movieId)

        // Then
        assertThat(result).isTrue()
        coVerify { localDataSource.removeMovieFromListCache(movieId, listId) }
    }

    @Test
    fun `removeMovieFromList should return false and not touch cache on remote failure`() = runTest {
        // Given
        coEvery { remoteDataSource.removeMovieFromList(any(), any(), any()) } returns Result.failure(Exception("error"))

        // When
        val result = repository.removeMovieFromList(1, 100)

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 0) { localDataSource.removeMovieFromListCache(any(), any()) }
    }

    @Test
    fun `getMovieListDetails should throw exception and not touch cache on remote failure`() = runTest {
        // Given
        coEvery { remoteDataSource.getDetails(any(), any()) } returns Result.failure(
            NetworkException.HttpLockedException(status = 1, message = "error")
        )

        // When & Then
        assertThrows<NetworkException.HttpLockedException> {
            repository.getMovieListDetails(1, 1)
        }
        coVerify(exactly = 0) { localDataSource.replaceMembershipsForList(any(), any()) }
    }

    @Test
    fun `getMovieListName should return name from local cache if available`() = runTest {
        // Given
        val listId = 1
        coEvery { localDataSource.getMovieList(listId) } returns MovieListLocalMock

        // When
        val result = repository.getMovieListName(listId)

        // Then
        assertThat(result).isEqualTo(MovieListLocalMock.name)
        coVerify(exactly = 0) { remoteDataSource.getDetails(any(), any()) }
    }

    @Test
    fun `getMovieListName should fetch from remote when not in local cache`() = runTest {
        // Given
        val listId = 1
        coEvery { localDataSource.getMovieList(listId) } returns null
        coEvery { remoteDataSource.getDetails(any(), any()) } returns Result.success(MovieListDetailsMock)

        // When
        val result = repository.getMovieListName(listId)

        // Then
        assertThat(result).isEqualTo(MovieListDetailsMock.name)
        coVerify { remoteDataSource.getDetails(listId, any()) }
    }

    private companion object {
        val MovieListResponseMock = ApiResponse(
            currentPage = 1,
            items = listOf(
                CustomMovieListResponse(
                    description = "desc",
                    id = 1,
                    itemCount = 1,
                    name = "My List"
                    )
            ),
            totalPages = 1,
            totalItems = 1
        )
        val MovieListDetailsMock = ListDetailsResponse(
            itemCount = 1,
            items = listOf(
                MovieRemote(
                    genreIds = emptyList(),
                    id = 100,
                    posterPath = "",
                    releaseDate = "2021-01-01",
                    voteAverage = 7.5,
                    name = ""
                )
            ),
            id = "1",
            name = "My Detailed List",
        )
        val MovieListLocalMock = MovieListLocal(
            id = 1,
            name = "Local My List",
            description = "Local desc",
            itemCount = 1
        )
    }
}