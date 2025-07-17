package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.dao.GenreInterestDao
import com.london.data.datasource.local.model.GenreInterestEntity
import com.london.data.datasource.local.model.PersonDtoLocal
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMovieDtoLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.SearchRemoteDataSource
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.data.datasource.util.CrashReporter
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class SearchRepositoryImplTest {

    @MockK(relaxed = true)
    private lateinit var searchMovieService: LocalDataSource<SearchMoviesLocal>
    private lateinit var searchTvShowService: LocalDataSource<SearchTvShowLocal>
    private lateinit var searchActorService: LocalDataSource<SearchActorsLocal>
    private lateinit var searchRemoteDataSource: SearchRemoteDataSource
    private lateinit var mockCrashReporter: CrashReporter
    private lateinit var genreInterestDao: GenreInterestDao
    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setUp() {
        searchTvShowService = mockk(relaxed = true)
        searchActorService = mockk(relaxed = true)
        searchMovieService = mockk(relaxed = true)
        searchRemoteDataSource = mockk(relaxed = true)
        mockCrashReporter = mockk<CrashReporter>(relaxed = true)
        genreInterestDao = mockk<GenreInterestDao>(relaxed = true)
        repository = SearchRepositoryImpl(
            localTvShowDataSource = searchTvShowService,
            localActorDataSource = searchActorService,
            localMovieDataSource = searchMovieService,
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
            repository.fetchAndSync(
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
    fun `searchForMoviesByID should return data from Remote if available`() = runTest {
        coEvery { searchRemoteDataSource.getMoviesByCategory(1 ,LANG, PAGE_NUMBER) } returns SearchMoviesRemoteMock
        val result = repository.searchForMoviesByCategory(1, LANG, PAGE_NUMBER)
        assertThat(result).isEqualTo(MovieList)
    }

    @Test
    fun `searchForMovies should return data from remote and cache it if local is null`() = runTest {
        coEvery { searchMovieService.getByQueryAndPage(NAME + LANG, PAGE_NUMBER) } returns null
        coEvery {
            searchRemoteDataSource.searchForMovies(
                any(),
                any(),
                any(),
                any()
            )
        } returns SearchMoviesRemoteMock
        val result = repository.searchForMovies(NAME, LANG, PAGE_NUMBER)
        assertThat(result).isEqualTo(MovieList)
        coVerify { searchMovieService.insert(any()) }
    }

    @Test
    fun `searchForMovies should return data from local if available`() = runTest {
        coEvery { searchMovieService.getByQueryAndPage(NAME + LANG, PAGE_NUMBER) } returns SearchMoviesLocalMock
        val result = repository.searchForMovies(NAME, LANG, PAGE_NUMBER)
        assertThat(result).isEqualTo(MovieList)
    }

    @Test
    fun `fetchAndSync reports to crashReporter when cacheBlock and networkBlock throw`() = runTest {
        val cacheException = RuntimeException("Cache failed")
        val networkException = RuntimeException("Network failed")

        testFetchAndSyncScenario(
            cacheBlockAction = { throw cacheException },
            networkBlockAction = { throw networkException },
            expectedException = networkException
        )
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
            searchTvShowService,
            searchActorService,
            searchMovieService,
            genreInterestDao,
            searchRemoteDataSource,
            mockCrashReporter
        )


        val actualException = assertThrows<RuntimeException> {
            repositoryWithNullCrashReporter.fetchAndSync(
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
    fun `searchForTvShows should return data from remote and cache it if local is null`() =
        runTest {
            coEvery { searchTvShowService.getByQueryAndPage(NAME + LANG, PAGE_NUMBER) } returns null
            coEvery {
                searchRemoteDataSource.searchForTvShows(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns SearchTvShowRemoteMock
            val result = repository.searchForTvShows(NAME, LANG, PAGE_NUMBER)
            assertThat(result).isEqualTo(TvShowList)
            coVerify { searchTvShowService.insert(any()) }
        }

    @Test
    fun `searchForTvShows should throw TvShowSearchFailedException when GetException is thrown`() =
        runTest {
            coEvery {
                searchRemoteDataSource.searchForTvShows(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws Exception()
            assertThrows<Exception> {
                repository.searchForTvShows(NAME, LANG, PAGE_NUMBER)
            }
        }

    @Test
    fun `searchForActors should return data from local if available`() = runTest {
        coEvery { searchActorService.getByQueryAndPage(NAME + LANG, PAGE_NUMBER) } returns SearchActorsLocalMock
        val result = repository.searchForActors(NAME, LANG, PAGE_NUMBER)
        assertThat(result).isEqualTo(ActorList)
    }

    @Test
    fun `searchForActors should throw ActorSearchFailedException when GetException is thrown`() =
        runTest {
            coEvery {
                searchRemoteDataSource.searchForActors(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws Exception()
            assertThrows<Exception> {
                repository.searchForActors(NAME, LANG, PAGE_NUMBER)
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
            })
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
            })
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


    private companion object {
        const val NAME = "Tom"
        const val LANG = "en-US"
        const val PAGE_NUMBER = 1

        val MovieList = PagedFetchResponse(
            PAGE_NUMBER,
            listOf(
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500",
                    releaseYear = 2020,
                    rating = 8,
                    genreIds = listOf(),
                )
            ),
            totalItems = 1,
            totalPages = 1
        )

        val TvShowList = PagedFetchResponse(
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

        val ActorList = PagedFetchResponse(
            PAGE_NUMBER,
            listOf(
                Actor(
                    id = 3,
                    name = "Tom Holland",
                    profilePicture = "https://image.tmdb.org/t/p/w500/tom_holland.jpg"
                )
            ),
            totalItems = 1,
            totalPages = 1
        )

        val SearchMoviesLocalMock = SearchMoviesLocal(
            query = NAME + LANG,
            page = PAGE_NUMBER,
            results = listOf(
                SearchMovieDtoLocal(
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
                    voteCount = 0
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        val SearchActorsLocalMock = SearchActorsLocal(
            query = NAME + LANG,
            page = PAGE_NUMBER,
            results = listOf(
                PersonDtoLocal(
                    adult = false,
                    gender = 2,
                    id = 3,
                    knownForDepartment = "",
                    name = "Tom Holland",
                    originalName = "Tom Holland",
                    popularity = 0.0,
                    profileUrl = "/tom_holland.jpg",
                    knownFor = emptyList()
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        val SearchMoviesRemoteMock = ApiResponse(
            currentPage = PAGE_NUMBER,
            items = listOf(
                SearchMovieRemote(
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
                    voteCount = 0
                )
            ),
            totalPages = 1,
            totalItems = 1
        )

        val SearchTvShowRemoteMock = ApiResponse(
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

        val SearchActorsRemoteMock = ApiResponse(
            currentPage = PAGE_NUMBER,
            items = listOf(
                SearchActorRemote(
                    adult = false,
                    gender = 2,
                    id = 3,
                    knownForDepartment = "",
                    name = "Tom Holland",
                    originalName = "Tom Holland",
                    popularity = 0.0,
                    profilePath = "",
                    knownFor = emptyList()
                )
            ),
            totalPages = 1,
            totalItems = 1
        )
    }
}