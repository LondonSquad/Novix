package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.local.GetException
import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.model.PersonDtoLocal
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMovieDtoLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowDtoLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.SearchRemoteDataSource
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.data.datasource.util.CrashReporter
import com.london.data.datasource.util.FirebaseCrashReporter
import com.london.domain.ActorSearchFailedException
import com.london.domain.MovieSearchFailedException
import com.london.domain.TvShowSearchFailedException
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows


class SearchRepositoryImplTest {

    private lateinit var searchMovieService: LocalDataSource<SearchMoviesLocal>
    private lateinit var searchTvShowService: LocalDataSource<SearchTvShowLocal>
    private lateinit var searchActorService: LocalDataSource<SearchActorsLocal>
    private lateinit var searchRemoteDataSource: SearchRemoteDataSource
    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setUp() {
        searchTvShowService = mockk(relaxed = true)
        searchActorService = mockk(relaxed = true)
        searchMovieService = mockk(relaxed = true)
        searchRemoteDataSource = mockk(relaxed = true)
        repository = SearchRepositoryImpl(
            localTvShowDataSource = searchTvShowService,
            localActorDataSource = searchActorService,
            localMovieDataSource = searchMovieService,
            remoteDataSource = searchRemoteDataSource,
            crashReporter = FirebaseCrashReporter()
        )
    }

    @Test
    fun `searchForMovies should return data from local if available`() = runTest {
        coEvery { searchMovieService.getByQuery(NAME + LANG) } returns SearchMoviesLocalMock
        val result = repository.searchForMovies(NAME, LANG, 1)
        assertThat(result).isEqualTo(MovieList)
    }

    @Test
    fun `searchForMovies should return data from remote and cache it if local is null`() = runTest {
        coEvery { searchMovieService.getByQuery(NAME + LANG) } returns null
        coEvery { searchRemoteDataSource.searchForMovies(any(), any(), any(), any()) } returns SearchMoviesRemoteMock
        val result = repository.searchForMovies(NAME, LANG, 1)
        assertThat(result).isEqualTo(MovieList)
        coVerify { searchMovieService.insert(any()) }
    }

    @Test
    fun `searchForMovies should throw MovieSearchFailedException when GetException is thrown`() =
        runTest {
            coEvery { searchMovieService.getByQuery(any()) } throws GetException("")
            assertThrows<MovieSearchFailedException> {
                repository.searchForMovies(NAME, LANG, 1)
            }
        }

    @Test
    fun `searchForTvShows should return data from local if available`() = runTest {
        coEvery { searchTvShowService.getByQuery(NAME + LANG) } returns SearchTvShowLocalMock
        val result = repository.searchForTvShows(NAME, LANG, 1)
        assertThat(result).isEqualTo(TvShowList)
    }

    @Test
    fun `searchForTvShows should return data from remote and cache it if local is null`() =
        runTest {
            coEvery { searchTvShowService.getByQuery(NAME + LANG) } returns null
            coEvery { searchRemoteDataSource.searchForTvShows(any(), any(), any(), any()) } returns SearchTvShowRemoteMock
            val result = repository.searchForTvShows(NAME, LANG, 1)
            assertThat(result).isEqualTo(TvShowList)
            coVerify { searchTvShowService.insert(any()) }
        }

    @Test
    fun `searchForTvShows should throw TvShowSearchFailedException when GetException is thrown`() =
        runTest {
            coEvery { searchTvShowService.getByQuery(any()) } throws GetException("")
            assertThrows<TvShowSearchFailedException> {
                repository.searchForTvShows(NAME, LANG, 1)
            }
        }

    @Test
    fun `searchForActors should return data from local if available`() = runTest {
        coEvery { searchActorService.getByQuery(NAME + LANG) } returns SearchActorsLocalMock
        val result = repository.searchForActors(NAME, LANG, 1)
        assertThat(result).isEqualTo(ActorList)
    }

    @Test
    fun `searchForActors should return data from remote and cache it if local is null`() = runTest {
        coEvery { searchActorService.getByQuery(NAME + LANG) } returns null
        coEvery { searchRemoteDataSource.searchForActors(any(), any(), any(), any()) } returns SearchActorsRemoteMock
        val result = repository.searchForActors(NAME, LANG, 1)
        assertThat(result).isEqualTo(ActorList)
        coVerify { searchActorService.insert(any()) }
    }

    @Test
    fun `searchForActors should throw ActorSearchFailedException when GetException is thrown`() =
        runTest {
            coEvery { searchActorService.getByQuery(any()) } throws GetException("")
            assertThrows<ActorSearchFailedException> {
                repository.searchForActors(NAME, LANG, 1)
            }
        }

    @Test
    fun `when search for actors should log unknown exceptions to crash reporter`() = runTest {
        val crashReporter = mockk<CrashReporter>(relaxed = true)
        repository = SearchRepositoryImpl(
            searchTvShowService,
            searchActorService,
            searchMovieService,
            searchRemoteDataSource,
            crashReporter
        )

        val exception = Exception("Unknown error")
        coEvery { searchActorService.getByQuery(any()) } throws exception

        repository.searchForActors(NAME, LANG, 1)

        coVerify { crashReporter.logException(exception) }
    }

    @Test
    fun `when search for movies should log unknown exceptions to crash reporter`() = runTest {
        val crashReporter = mockk<CrashReporter>(relaxed = true)
        repository = SearchRepositoryImpl(
            searchTvShowService,
            searchActorService,
            searchMovieService,
            searchRemoteDataSource,
            crashReporter
        )

        val exception = Exception("Unknown error")
        coEvery { searchMovieService.getByQuery(any()) } throws exception

        repository.searchForMovies(NAME, LANG, 1)

        coVerify { crashReporter.logException(exception) }
    }
    @Test
    fun `search for tv shows should log unknown exceptions to crash reporter`() = runTest {
        val crashReporter = mockk<CrashReporter>(relaxed = true)
        repository = SearchRepositoryImpl(
            searchTvShowService,
            searchActorService,
            searchMovieService,
            searchRemoteDataSource,
            crashReporter
        )

        val exception = Exception("Unknown error")
        coEvery { searchTvShowService.getByQuery(any()) } throws exception

        repository.searchForTvShows(NAME, LANG, 1)

        coVerify { crashReporter.logException(exception) }
    }


    private companion object {
        const val NAME = "Tom"
        const val LANG = "en-US"

        val MovieList = listOf(
            Movie(
                id = 1,
                name = "",
                posterPicture = "https://image.tmdb.org/t/p/w500",
                releaseYear = 2020,
                rating = 8,
                genreIds = listOf()
            )
        )

        val TvShowList = listOf(
            TvShow(
                id = 2,
                name = "",
                posterPicture = "https://image.tmdb.org/t/p/w500",
                releaseYear = 2020,
                rating = 10,
                genres = listOf()
            )
        )

        val ActorList = listOf(
            Actor(
                id = 3,
                name = "Tom Holland",
                profilePicture = "https://image.tmdb.org/t/p/w500"
            )
        )

        val SearchMoviesLocalMock = SearchMoviesLocal(
            query = NAME + LANG,
            page = 1,
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

        val SearchTvShowLocalMock = SearchTvShowLocal(
            query = NAME + LANG,
            page = 1,
            results = listOf(
                SearchTvShowDtoLocal(
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
            totalResults = 1
        )

        val SearchActorsLocalMock = SearchActorsLocal(
            query = NAME + LANG,
            page = 1,
            results = listOf(
                PersonDtoLocal(
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
            totalResults = 1
        )

        val SearchMoviesRemoteMock = ApiResponse(
            currentPage = 1,
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
            currentPage = 1,
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
            currentPage = 1,
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