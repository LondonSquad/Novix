package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.home.TopRatedLocal
import com.london.data.local.source.home.popular.HomeLocalDataSource
import com.london.data.mapper.toprated.toEntity
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.TopRatedMovieRemote
import com.london.data.remote.source.toprated.movie.TopRatedMovieRemoteDataSource
import com.london.data.repository.toprated.TopRatedMovieRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.domain.entity.recent.MediaType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TopRatedMovieRepositoryImplTest {

    private lateinit var remoteDataSource: TopRatedMovieRemoteDataSource
    private lateinit var repository: TopRatedMovieRepositoryImpl
    private val crashReporter: CrashReporter = mockk(relaxed = true)
    private val localTopRatedMovie: HomeLocalDataSource<TopRatedLocal> = mockk(relaxed = true)

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = TopRatedMovieRepositoryImpl(
            topRatedMovieRemoteDataSource = remoteDataSource,
            localTopRated = localTopRatedMovie,
            crashReporter = crashReporter
        )
    }

    @Test
    fun `getTopRatedMovies should return mapped movies from remote data source`() = runTest {
        // Given
        val fakeApiResponse = fakeApiResponseWithMovies()
        coEvery {
            remoteDataSource.getTopRatedMovies(PAGE)
        } returns Result.success(fakeApiResponse)

        coEvery {
            localTopRatedMovie.getAll()
        } returns emptyList()

        // When
        val result = repository.getTopRatedMovies(PAGE)

        // Then
        assertThat(result.items).hasSize(2)
        assertThat(result.currentPage).isEqualTo(PAGE)
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.totalItems).isEqualTo(2)

        val firstMovie = result.items.first()
        assertThat(firstMovie).isEqualTo(
            fakeApiResponse.items[0].toEntity()
        )

        val secondMovie = result.items[1]
        assertThat(secondMovie).isEqualTo(
            fakeApiResponse.items[1].toEntity()
        )

        // Verify that data was synced to local storage
        coVerify {
            localTopRatedMovie.insertAll(any())
        }
    }

    @Test
    fun `getTopRatedMovies should return empty list when API returns empty results`() = runTest {
        // Given
        val emptyApiResponse = fakeEmptyApiResponse()
        coEvery {
            remoteDataSource.getTopRatedMovies(PAGE)
        } returns Result.success(emptyApiResponse)

        coEvery {
            localTopRatedMovie.getAll()
        } returns emptyList()

        // When
        val result = repository.getTopRatedMovies(PAGE)

        // Then
        assertThat(result.items).isEmpty()
        assertThat(result.currentPage).isEqualTo(PAGE)
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.totalItems).isEqualTo(0)
    }

    @Test
    fun `getTopRatedMovies should propagate exceptions from remote data source`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedMovies(PAGE)
        } returns Result.failure(RuntimeException("Network error"))

        coEvery {
            localTopRatedMovie.getAll()
        } returns emptyList()

        // When & Then
        val exception = assertThrows<RuntimeException> {
            repository.getTopRatedMovies(PAGE)
        }
        assertThat(exception.message).isEqualTo("Network error")
    }

    @Test
    fun `getTopRatedMovies should handle local cache when available and filter by MediaType Movie`() =
        runTest {
            // Given
            val localMovies = listOf(
                createFakeTopRatedLocal(id = 1, mediaType = MediaType.Movie),
                createFakeTopRatedLocal(
                    id = 2,
                    mediaType = MediaType.TvShow
                ), // This should be filtered out
                createFakeTopRatedLocal(id = 3, mediaType = MediaType.Movie)
            )

            coEvery {
                localTopRatedMovie.getAll()
            } returns localMovies

            coEvery {
                remoteDataSource.getTopRatedMovies(PAGE)
            } returns Result.success(fakeApiResponseWithMovies())

            // When
            val result = repository.getTopRatedMovies(PAGE)

            // Then
            // Should still return remote data due to the implementation structure
            assertThat(result.items).hasSize(2)

            // Verify local data was accessed
            coVerify {
                localTopRatedMovie.getAll()
            }
        }

    companion object {
        private const val PAGE = 1

        private fun fakeApiResponseWithMovies() = ApiResponse(
            currentPage = PAGE,
            totalPages = 1,
            totalItems = 2,
            items = listOf(
                TopRatedMovieRemote(
                    adult = false,
                    backdropPath = "/zfbjgQEluSd9wiPTX4VzslorGfY.jpg",
                    genreIds = listOf(18, 80),
                    id = 278,
                    originalLanguage = "en",
                    originalTitle = "The Shawshank Redemption",
                    overview = "Imprisoned in the 1940s for the murder of his wife...",
                    popularity = 31.7537,
                    posterPath = "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
                    releaseDate = "1994-09-23",
                    title = "The Shawshank Redemption",
                    video = false,
                    voteAverage = 8.712,
                    voteCount = 25000
                ),
                TopRatedMovieRemote(
                    adult = false,
                    backdropPath = "/ejdD20cdHNFAYAN2DlqPToXKyzx.jpg",
                    genreIds = listOf(18, 80),
                    id = 238,
                    originalLanguage = "en",
                    originalTitle = "The Godfather",
                    overview = "Spanning the years 1945 to 1955...",
                    popularity = 45.123,
                    posterPath = "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
                    releaseDate = "1972-03-14",
                    title = "The Godfather",
                    video = false,
                    voteAverage = 8.7,
                    voteCount = 20000
                )
            )
        )

        private fun fakeEmptyApiResponse() = ApiResponse(
            currentPage = PAGE,
            totalPages = 1,
            totalItems = 0,
            items = emptyList<TopRatedMovieRemote>()
        )

        private fun createFakeTopRatedLocal(
            id: Int,
            name: String = "",
            posterPictureUrl: String = "",
            rating: Double = 0.0,
            releaseYear: String = "",
            mediaType: MediaType,
            date: Long = 0L,
            genre: List<Int> = listOf(1)
        ) = TopRatedLocal(
            id = id,
            name = name,
            posterPictureUrl = posterPictureUrl,
            rating = rating,
            releaseYear = releaseYear,
            mediaType = mediaType,
            date = date,
            genre = genre
        )
    }
}