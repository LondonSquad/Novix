package com.london.data.repository.discover

import com.google.common.truth.Truth.assertThat
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.MovieRemote
import com.london.data.remote.model.search.model.SearchTvShowRemote
import com.london.data.remote.source.discover.DiscoverRemoteDataSource
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class DiscoverRepositoryImplTest {

    private lateinit var discoverRemoteDataSource: DiscoverRemoteDataSource
    private lateinit var repository: DiscoverRepositoryImpl

    @Before
    fun setUp() {
        discoverRemoteDataSource = mockk(relaxed = true)
        repository = DiscoverRepositoryImpl(discoverRemoteDataSource)
    }

    @Test
    fun `getMoviesByCategory should return data from remote if available`() = runTest {
        //Given
        coEvery {
            discoverRemoteDataSource.getMoviesByCategory(
                CATEGORY_ID,
                PAGE_NUMBER
            )
        } returns Result.success(SearchMoviesRemoteMock)
        //When
        val result = repository.getMoviesByCategory(
            CATEGORY_ID,
            PAGE_NUMBER
        )
        //Then
        assertThat(result).isEqualTo(MovieList)
    }

    @Test
    fun `getMoviesByCategory should throw HttpLockedException when API returns 423`() = runTest {
        //Given
        coEvery {
            discoverRemoteDataSource.getMoviesByCategory(CATEGORY_ID, PAGE_NUMBER)
        } returns Result.failure(NetworkException.HttpLockedException("Resource locked"))
        //When //Then
        assertThrows<NetworkException.HttpLockedException> {
            repository.getMoviesByCategory(CATEGORY_ID, PAGE_NUMBER)
        }
    }

    @Test
    fun `getTvShowsByCategory should return data from data source if available`() = runTest {
        //Given
        coEvery {
            discoverRemoteDataSource.getTvShowsByCategoryId(any(), any())
        } returns Result.success(SearchTvShowRemoteMock)
        //When
        val result = repository.getTvShowsByCategory(categoryId = 1, PAGE_NUMBER)
        //Then
        assertThat(result).isEqualTo(TvShowList)
    }

    @Test
    fun `searchForTvShowsByCategory should throw HttpLockedException when API returns 423`() =
        runTest {
            //Given
            coEvery {
                discoverRemoteDataSource.getTvShowsByCategoryId(CATEGORY_ID, PAGE_NUMBER)
            } returns Result.failure(NetworkException.HttpLockedException("Resource locked"))
            //When //Then
            assertThrows<NetworkException.HttpLockedException> {
                repository.getTvShowsByCategory(CATEGORY_ID, PAGE_NUMBER)
            }
        }


    private companion object {
        const val CATEGORY_ID = 2
        const val PAGE_NUMBER = 1
        val MovieList = PagedFetchResponse(
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

        private val SearchTvShowRemoteMock = ApiResponse(
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

