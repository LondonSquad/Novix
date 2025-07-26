package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.source.toprated.movie.TopRatedMovieRemoteDataSource
import com.london.data.remote.model.toprated.movie.model.TopRatedMovieRemote
import com.london.data.mapper.toprated.toEntity
import com.london.domain.entity.toprated.TopRatedMovie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TopRatedMovieRepositoryImplTest {

    private lateinit var remoteDataSource: TopRatedMovieRemoteDataSource
    private lateinit var repository: TopRatedMovieRepoImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = TopRatedMovieRepoImpl(remoteDataSource)
    }

    @Test
    fun `getTopRatedMovies should map remote movie list correctly`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedMovies(PAGE, LANGUAGE, REGION)
        } returns fakeApiResponseWithMovies()

        // When
        val result: List<TopRatedMovie> = repository.getTopRatedMovies(PAGE, LANGUAGE, REGION)

        // Then
        assertThat(result).hasSize(2)

        val firstMovie = result.first()
        assertThat(firstMovie).isEqualTo(
            fakeApiResponseWithMovies().items[0].toEntity()
        )

        val secondMovie = result[1]
        assertThat(secondMovie).isEqualTo(
            fakeApiResponseWithMovies().items[1].toEntity()
        )
    }

    @Test
    fun `getTopRatedMovies should return empty list when API returns empty results`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedMovies(PAGE, LANGUAGE, REGION)
        } returns fakeEmptyApiResponse()

        // When
        val result = repository.getTopRatedMovies(PAGE, LANGUAGE, REGION)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTopRatedMovies should propagate exceptions`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getTopRatedMovies(PAGE, LANGUAGE, REGION)
        } throws RuntimeException("Network error")

        // When && Then
        val ex = assertThrows<RuntimeException> {
            repository.getTopRatedMovies(PAGE, LANGUAGE, REGION)
        }
        assertThat(ex.message).isEqualTo("Network error")
    }

    companion object {
        private const val PAGE = 1
        private const val LANGUAGE = "en-US"
        private const val REGION = "US"

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
    }
}
