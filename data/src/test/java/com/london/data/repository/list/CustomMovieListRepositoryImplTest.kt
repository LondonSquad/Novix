package com.london.data.repository.list

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.customLists.MovieListLocal
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.customLists.CustomMovieListLocalDataSource
import com.london.data.remote.exception.ResponseException
import com.london.data.remote.model.list.CreateCustomListResponse
import com.london.data.remote.model.list.ListDetailsResponse
import com.london.data.remote.model.search.SearchMovieRemote
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.domain.entity.language.AppLanguage
import com.london.domain.service.AppPreferencesService
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
    fun `deleteMovieList returns true and removes cache on success`() = runTest {
        // Given
        coEvery { remoteDataSource.delete(any(), any()) } returns Result.success(mockk())
        val listId = 1

        // When
        val result = repository.deleteMovieList(listId)

        // Then
        assertThat(result).isTrue()
        coVerify { localDataSource.removeMovieListCache(listId) }
    }

    @Test
    fun `deleteMovieList throws and does not remove cache on failure`() = runTest {
        // Given
        coEvery { remoteDataSource.delete(any(), any()) } throws Exception("error")
        val listId = 1

        // When & Then
        assertThrows<Exception> {
            repository.deleteMovieList(listId)
        }

        coVerify(exactly = 0) { localDataSource.removeMovieListCache(any()) }
    }

    @Test
    fun `createMovieList returns true and adds cache on success`() = runTest {
        // Given
        val name = "My List"
        coEvery { remoteDataSource.create(any(), any(), any()) } returns Result.success(
            CreateCustomListResponse(id = 1)
        )

        // When
        val result = repository.createMovieList(name)

        // Then
        assertThat(result).isTrue()
        coVerify { localDataSource.addMovieListCache(any()) }
    }

    @Test
    fun `createMovieList throws and does not add cache on failure`() = runTest {
        // Given
        coEvery { remoteDataSource.create(any(), any(), any()) } returns Result.failure(Exception("error"))

        // When & Then
        assertThrows<Exception> {
            repository.createMovieList("My List")
        }
        coVerify(exactly = 0) { localDataSource.addMovieListCache(any()) }
    }

    @Test
    fun `addMovieToList returns true and updates cache on success`() = runTest {
        // Given
        val listId = 1
        val movieId = 100
        coEvery { remoteDataSource.addMovieToList(any(), any(), any()) } returns Result.success(mockk())
        coEvery { localDataSource.getMovieList(listId) } returns MovieListLocal(
            id = listId,
            name = "",
            description = "",
            itemCount = 0
        )

        // When
        val result = repository.addMovieToList(listId, movieId)

        // Then
        assertThat(result).isTrue()
        coVerify { localDataSource.addMovieToListCache(movieId, listId) }
    }

    @Test
    fun `addMovieToList throws and does not update cache on failure`() = runTest {
        // Given
        coEvery {
            remoteDataSource.addMovieToList(
                any(),
                any(),
                any()
            )
        } returns Result.failure(Exception("error"))

        // When & Then
        assertThrows<Exception> {
            repository.addMovieToList(1, 100)
        }
        coVerify(exactly = 0) { localDataSource.addMovieToListCache(any(), any()) }
    }

    @Test
    fun `removeMovieFromList returns true and updates cache on success`() = runTest {
        // Given
        val listId = 1
        val movieId = 100
        coEvery { remoteDataSource.removeMovieFromList(any(), any(), any()) } returns Result.success(mockk())
        coEvery { localDataSource.getMovieList(listId) } returns MovieListLocal(
            id = listId,
            name = "",
            description = "",
            itemCount = 1
        )

        // When
        val result = repository.removeMovieFromList(listId, movieId)

        // Then
        assertThat(result).isTrue()
        coVerify { localDataSource.removeMovieFromListCache(movieId, listId) }
    }

    @Test
    fun `removeMovieFromList throws and does not update cache on failure`() = runTest {
        // Given
        coEvery {
            remoteDataSource.removeMovieFromList(
                any(),
                any(),
                any()
            )
        } returns Result.failure(Exception("error"))

        // When & Then
        assertThrows<Exception> {
            repository.removeMovieFromList(1, 100)
        }
        coVerify(exactly = 0) { localDataSource.removeMovieFromListCache(any(), any()) }
    }

    @Test
    fun `getMovieListDetails returns paged response on success`() = runTest {
        // Given
        val listId = 1
        val page = 1
        val response = ListDetailsResponse(
            itemCount = 1,
            items = listOf(
                SearchMovieRemote(
                    genreIds = emptyList(),
                    id = 100,
                    posterPath = "",
                    releaseDate = "2021-01-01",
                    voteAverage = 7.5,
                    name = "Movie"
                )
            ),
            id = "1",
            name = "My List"
        )
        coEvery { remoteDataSource.getDetails(any(), any()) } returns Result.success(response)

        // When
        val result = repository.getMovieListDetails(listId, page)

        // Then
        assertThat(result.totalItems).isEqualTo(1)
    }

    @Test
    fun `getMovieListDetails throws on failure`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getDetails(
                any(),
                any()
            )
        } returns Result.failure(ResponseException(code = 1, message = "error"))

        // When & Then
        assertThrows<ResponseException> {
            repository.getMovieListDetails(1, 1)
        }
    }

    @Test
    fun `getMovieListName returns name from local if available`() = runTest {
        // Given
        val listId = 1
        coEvery { localDataSource.shouldRefreshCache() } returns false
        coEvery { localDataSource.getMovieList(listId) } returns MovieListLocalMock

        // When
        val result = repository.getMovieListName(listId)

        // Then
        assertThat(result).isEqualTo(MovieListLocalMock.name)
    }

    @Test
    fun `getMovieListName returns empty if not available locally`() = runTest {
        // Given
        val listId = 1
        coEvery { localDataSource.shouldRefreshCache() } returns false
        coEvery { localDataSource.getMovieList(listId) } returns null

        // When
        val result = repository.getMovieListName(listId)

        // Then
        assertThat(result).isEmpty()
    }

    private companion object {
        val MovieListLocalMock = MovieListLocal(
            id = 1,
            name = "Local My List",
            description = "Local desc",
            itemCount = 1
        )
    }
}
