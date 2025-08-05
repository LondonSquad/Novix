package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.local.database.dao.search.GenreInterestDao
import com.london.data.local.model.search.GenreInterestEntity
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.repository.search.SearchRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
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
      fetchAndSync(
                cacheBlockAction,
                networkBlockAction,
                syncBlockAction,
                crashReporterToUse
            )
        }

        assertThat(actualException).isInstanceOf(expectedException::class.java)
        assertThat(actualException.message).isEqualTo(expectedException.message)


        if (crashReporterToUse != null) {
            coVerify(exactly = PAGE_NUMBER) { crashReporterToUse.logException(expectedLoggedException) }
        } else {
            coVerify(exactly = 0) { mockCrashReporter.logException(any()) }
        }
    }

    @Test
    fun `fetchAndSync reports to crashReporter when networkBlock throws and cache is null`() = runTest {
        val networkException = RuntimeException("Network failed")

        testFetchAndSyncScenario(
            cacheBlockAction = { null },
            networkBlockAction = { throw networkException },
            expectedException = networkException
        )
    }

    @Test
    fun `fetchAndSync reports to crashReporter when syncBlock throws and cache is null`() = runTest {
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
        val repositoryWithNullCrashReporter = SearchRepositoryImpl(
            genreInterestDao,
            searchRemoteDataSource,
            mockCrashReporter
        )


        val actualException = assertThrows<RuntimeException> {
          fetchAndSync(
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
        val genreId = 10
        val mediaType = "movie"

        coEvery { genreInterestDao.getGenreInterest(genreId, mediaType) } returns null

        repository.incrementGenreInterest(genreId, mediaType)

        coVerify {
            genreInterestDao.insertGenreInterest(match {
                it.genreId == genreId && it.mediaType == mediaType && it.count == 1
            }
            )
        }
    }

    @Test
    fun `incrementGenreInterest should update when record exists`() = runTest {
        val genreId = 20
        val mediaType = "tv"
        val existing = GenreInterestEntity(genreId, mediaType, count = 5)
        coEvery { genreInterestDao.getGenreInterest(genreId, mediaType) } returns existing
        repository.incrementGenreInterest(genreId, mediaType)
        coVerify {
            genreInterestDao.updateGenreInterest(match {
                it.genreId == genreId && it.mediaType == mediaType && it.count == existing.count + 1
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
        val genreId = 99
        val mediaType = "movie"
        val exception = RuntimeException("DAO failure")

        coEvery { genreInterestDao.getGenreInterest(genreId, mediaType) } throws exception

        repository.incrementGenreInterest(genreId, mediaType)

        coVerify { mockCrashReporter.logException(exception) }
    }

    @Test
    fun `incrementGenreInterest inserts new genre when not existing`() = runTest {
        val genreId = 1
        val mediaType = "movie"

        coEvery { genreInterestDao.getGenreInterest(genreId, mediaType) } returns null
        coJustRun { genreInterestDao.insertGenreInterest(any()) }

        repository.incrementGenreInterest(genreId, mediaType)

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
        } throws NetworkException.UnAuthorizedException("401 Unauthorized")

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.searchForTvShows(query, page)
        }
    }

    @Test
    fun `searchForTvShows should throw TimeoutException when API times out`() = runTest {
        val query = "Breaking Bad"
        val page = 1

        coEvery {
            searchRemoteDataSource.searchForTvShows(query, false, page)
        } throws NetworkException.TimeoutException("Request timed out")

        assertThrows<NetworkException.TimeoutException> {
            repository.searchForTvShows(query, page)
        }
    }

    @Test
    fun `searchForActors should throw ValidationException when API returns 422`() = runTest {
        val query = "Leonardo DiCaprio"
        val page = 1

        coEvery {
            searchRemoteDataSource.searchForActors(query, false, page)
        } throws NetworkException.ValidationException("Invalid query")

        assertThrows<NetworkException.ValidationException> {
            repository.searchForActors(query, page)
        }
    }

    private companion object {
      private  const val NAME = "Tom"
       private const val LANG = "en-US"
       private const val PAGE_NUMBER = 1

        private    val MovieList = PagedFetchResponse(
            PAGE_NUMBER,
            listOf(
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "https://image.tmdb.org/t/p/w500",
                    releaseYear = 2020,
                    rating = 8,
                    genreIds = listOf(),
                )
            ),
            totalItems = 1,
            totalPages = 1
        )

        private   val TvShowList = PagedFetchResponse(
            PAGE_NUMBER,
            listOf(
                TvShow(
                    id = 2,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500",
                    releaseYear = 2020,
                    rating = 10,
                    genres = listOf(),
                )
            ),
            totalItems = 1,
            totalPages = 1
        )

        private val ActorList = PagedFetchResponse(
            PAGE_NUMBER,
            listOf(
                Actor(
                    id = 3,
                    name = "Tom Holland",
                    profilePictureUrl = "https://image.tmdb.org/t/p/w500/tom_holland.jpg",
                    characterName = ""
                )
            ),
            totalItems = 1,
            totalPages = 1
        )

        private val SearchMoviesRemoteMock = ApiResponse(
            currentPage = PAGE_NUMBER,
            items = listOf(
                MovieRemote(
                    adult = false,
                    backdropPath = null,
                    genreIds = emptyList(),
                    id = 1,
                    originalLanguage = "en",
                    originalTitle = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "",
                    releaseDate = "2020-06-15",
                    title = "",
                    video = false,
                    voteAverage = 8.0,
                    voteCount = 0,
                    originCountry = listOf(""),
                    originalName = "",
                    firstAirDate = "",
                    name = "",
                )
            ),
            totalPages = 1,
            totalItems = 1
        )

        private   val SearchTvShowRemoteMock = ApiResponse(
            currentPage = PAGE_NUMBER,
            items = listOf(
                SearchTvShowRemote(
                    adult = false,
                    backdropPath = "",
                    genreIds = emptyList(),
                    id = 2,
                    originCountry = emptyList(),
                    originalLanguage = "en",
                    originalName = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "",
                    firstAirDate = "2020-07-20",
                    name = "",
                    voteAverage = 10.0,
                    voteCount = 0
                )
            ),
            totalPages = 1,
            totalItems = 1
        )
    }
}
