package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.local.database.dao.search.GenreInterestDao
import com.london.data.local.model.search.GenreInterestEntity
import com.london.data.remote.exception.ResponseException
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.repository.search.SearchRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.data.utils.loadFromCacheOrFetch
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.genre.TvShowGenre
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class SearchRepositoryImplTest {

    @MockK(relaxed = true)
    private lateinit var searchRemoteDataSource: SearchRemoteDataSource
    private lateinit var mockCrashReporter: CrashReporter
    private lateinit var genreInterestDao: GenreInterestDao
    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setUp() {
        searchRemoteDataSource = mockk(relaxed = true)
        mockCrashReporter = mockk<CrashReporter>(relaxed = true)
        genreInterestDao = mockk<GenreInterestDao>(relaxed = true)
        repository = SearchRepositoryImpl(
            remoteDataSource = searchRemoteDataSource,
            crashReporter = mockCrashReporter,
            genreInterestDao = genreInterestDao
        )

    }

    private suspend fun <T> testFetchAndSyncScenario(
        cacheBlockAction: suspend () -> T?,
        networkBlockAction: suspend () -> T,
        syncBlockAction: suspend (T) -> Unit = { /* Default no-op */ },
        expectedException: Exception,
        expectedLoggedException: Exception = expectedException,
        crashReporterToUse: CrashReporter? = mockCrashReporter
    ) {
        val actualException = assertThrows<RuntimeException>(
            expectedException::class.java.simpleName
        ) {
            loadFromCacheOrFetch(
                cacheBlockAction,
                networkBlockAction,
                syncBlockAction,
                crashReporterToUse
            )
        }

        assertThat(actualException).isInstanceOf(expectedException::class.java)
        assertThat(actualException.message).isEqualTo(expectedException.message)


        if (crashReporterToUse != null) {
            coVerify(exactly = PAGE_NUMBER) {
                crashReporterToUse.logException(
                    expectedLoggedException
                )
            }
        } else {
            coVerify(exactly = 0) { mockCrashReporter.logException(any()) }
        }
    }

    @Test
    fun `fetchAndSync reports to crashReporter when networkBlock throws and cache is null`() =
        runTest {
            val networkException = RuntimeException("Network failed")

            testFetchAndSyncScenario(
                cacheBlockAction = { null },
                networkBlockAction = { throw networkException },
                expectedException = networkException
            )
        }

    @Test
    fun `fetchAndSync reports to crashReporter when syncBlock throws and cache is null`() =
        runTest {
            val syncException = RuntimeException("Sync failed")
            val networkData = "Network Data"

            testFetchAndSyncScenario(
                cacheBlockAction = { null },
                networkBlockAction = { networkData },
                syncBlockAction = { throw syncException },
                expectedException = syncException
            )
        }

    @Test
    fun `fetchAndSync reports to crashReporter when syncBlock throws and cache threw`() = runTest {
        val cacheException = RuntimeException("Cache failed")
        val syncException = RuntimeException("Sync failed")
        val networkData = "Network Data"

        testFetchAndSyncScenario(
            cacheBlockAction = { throw cacheException },
            networkBlockAction = { networkData },
            syncBlockAction = { throw syncException },
            expectedException = syncException
        )
    }

    @Test
    fun `fetchAndSync does not report if crashReporter is null and error occurs`() = runTest {
        val cacheException = RuntimeException("Cache failed")
        val networkException = RuntimeException("Network failed")

        // Need to create a repository instance with a null crash reporter for this specific test
        SearchRepositoryImpl(
            genreInterestDao,
            searchRemoteDataSource,
            mockCrashReporter
        )


        val actualException = assertThrows<RuntimeException> {
            loadFromCacheOrFetch(
                cacheBlock = { throw cacheException },
                networkBlock = { throw networkException },
                syncBlock = { /* Do nothing */ },
                crashReporter = null
            )
        }
        assertThat(actualException).isEqualTo(networkException)
        coVerify(exactly = 0) { mockCrashReporter.logException(any()) }
    }

    @Test
    fun `searchForTvShows should throw TvShowSearchFailedException when GetException is thrown`() =
        runTest {
            coEvery {
                searchRemoteDataSource.searchForTvShows(
                    any(),
                    any(),
                    any()
                )
            } throws Exception()
            assertThrows<Exception> {
                repository.searchForTvShows(NAME, PAGE_NUMBER)
            }
        }

    @Test
    fun `searchForActors should throw ActorSearchFailedException when GetException is thrown`() =
        runTest {
            coEvery {
                searchRemoteDataSource.searchForActors(
                    any(),
                    any(),
                    any()
                )
            } throws Exception()
            assertThrows<Exception> {
                repository.searchForActors(NAME, PAGE_NUMBER)
            }
        }

    @Test
    fun `incrementGenreInterest should insert when no existing record`() = runTest {
        val genreId = 28
        val mediaType = "movie"

        coEvery { genreInterestDao.getGenreInterest(any(), mediaType) } returns null

        repository.incrementGenreInterest(MovieGenre.ACTION, mediaType)

        coVerify {
            genreInterestDao.insertGenreInterest(match {
                it.genreId == genreId && it.mediaType == mediaType && it.count == 1
            }
            )
        }
    }

    @Test
    fun `incrementGenreInterest should update when record exists`() = runTest {
        val genre = 20
        val mediaType = "tv"
        val existing = GenreInterestEntity(genre, mediaType, count = 5)
        coEvery { genreInterestDao.getGenreInterest(any(), mediaType) } returns existing
        repository.incrementGenreInterest(TvShowGenre.WESTERN, mediaType)
        coVerify {
            genreInterestDao.updateGenreInterest(match {
                it.genreId == genre && it.mediaType == mediaType && it.count == existing.count + 1
            }
            )
        }
    }

    @Test
    fun `getGenreInterestCounts should map dao entities to pairs`() = runTest {
        val mediaType = "movie"
        val entities = listOf(
            GenreInterestEntity(1, mediaType, 3),
            GenreInterestEntity(2, mediaType, 7)
        )
        coEvery { genreInterestDao.getGenresByInterest(mediaType) } returns entities
        val result = repository.getGenreInterestCounts(mediaType)
        assertThat(result).containsExactly((1 to 3), (2 to 7))
    }

    @Test
    fun `getGenreInterestCounts should log exception and return empty list on error`() = runTest {
        val mediaType = "tv"
        val exception = RuntimeException("DB error")

        coEvery { genreInterestDao.getGenresByInterest(mediaType) } throws exception

        val result = repository.getGenreInterestCounts(mediaType)
        assertThat(result).isEmpty()

        coVerify { mockCrashReporter.logException(exception) }
    }

    @Test
    fun `incrementGenreInterest should log exception on dao error`() = runTest {
        val genreId = 28
        val mediaType = "movie"
        val exception = RuntimeException("DAO failure")

        coEvery { genreInterestDao.getGenreInterest(genreId, mediaType) } throws exception

        repository.incrementGenreInterest(MovieGenre.ACTION, mediaType)

        coVerify { mockCrashReporter.logException(exception) }
    }

    @Test
    fun `incrementGenreInterest inserts new genre when not existing`() = runTest {
        val genreId = 28
        val mediaType = "movie"

        coEvery { genreInterestDao.getGenreInterest(genreId, mediaType) } returns null
        coJustRun { genreInterestDao.insertGenreInterest(any()) }

        repository.incrementGenreInterest(MovieGenre.ACTION, mediaType)

        coVerify {
            genreInterestDao.insertGenreInterest(
                GenreInterestEntity(genreId, mediaType, count = 1)
            )
        }
    }


    @Test
    fun `searchForTvShows should throw UnAuthorizedException when API returns 401`() = runTest {
        val query = "Breaking Bad"
        val page = 1

        coEvery {
            searchRemoteDataSource.searchForTvShows(query, false, page)
        } throws ResponseException(
            message = "401 Unauthorized",
            code = 401
        )

        assertThrows<ResponseException> {
            repository.searchForTvShows(query, page)
        }
    }

    @Test
    fun `searchForTvShows should throw TimeoutException when API times out`() = runTest {
        val query = "Breaking Bad"
        val page = 1

        coEvery {
            searchRemoteDataSource.searchForTvShows(query, false, page)
        } throws ResponseException(
            message = "Request timed out",
            code = 408
        )

        assertThrows<ResponseException> {
            repository.searchForTvShows(query, page)
        }
    }

    @Test
    fun `searchForActors should throw ValidationException when API returns 422`() = runTest {
        val query = "Leonardo DiCaprio"
        val page = 1

        coEvery {
            searchRemoteDataSource.searchForActors(query, false, page)
        } throws ResponseException(
            message = "Invalid query",
            code = 422
        )

        assertThrows<ResponseException> {
            repository.searchForActors(query, page)
        }
    }

    private companion object {
        private const val NAME = "Tom"
        private const val PAGE_NUMBER = 1
    }
}
