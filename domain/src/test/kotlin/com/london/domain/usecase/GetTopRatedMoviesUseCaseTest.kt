package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.repository.TopRatedMovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedMoviesUseCaseTest {

    private lateinit var repository: TopRatedMovieRepository
    private lateinit var getTopRatedMovies: GetTopRatedMoviesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getTopRatedMovies = GetTopRatedMoviesUseCase(repository)
    }

    @Test
    fun `should return movies when repository returns valid response`() = runTest {
        // Given
        val mockPagedResponse = PagedFetchResponse(
            items = mockTopRatedMovies,
            currentPage = PAGE,
            totalPages = TOTAL_PAGES,
            totalItems = TOTAL_ITEMS

        )

        coEvery {
            repository.getTopRatedMovies(PAGE)
        } returns mockPagedResponse

        // When
        val result = getTopRatedMovies(PAGE)

        // Then
        assertThat(result).isEqualTo(mockPagedResponse)
        assertThat(result.items[0].title).isEqualTo("The Shawshank Redemption")
        assertThat(result.items[1].title).isEqualTo("The Godfather")
    }

    @Test
    fun `should return empty list when repository returns empty response`() = runTest {
        // Given
        val emptyPagedResponse = PagedFetchResponse(
            currentPage = PAGE,
            totalPages = TOTAL_PAGES,
            totalItems = TOTAL_PAGES,
            items = emptyList<TopRatedMovie>()
        )

        coEvery {
            repository.getTopRatedMovies(PAGE)
        } returns emptyPagedResponse

        // When
        val result = getTopRatedMovies(PAGE)

        // Then
        assertThat(result.items).isEmpty()
    }

    @Test
    fun `should throw RuntimeException when repository throws`() = runTest {
        // Given
        coEvery {
            repository.getTopRatedMovies(PAGE)
        } throws RuntimeException()

        // When & Then
        assertThrows<RuntimeException> {
            getTopRatedMovies(PAGE)
        }
    }

    companion object {
        private const val PAGE = 1
        private const val TOTAL_PAGES = 2
        private const val TOTAL_ITEMS = 100

        private val mockMovie1 = TopRatedMovie(
            id = 278,
            title = "The Shawshank Redemption",
            originalTitle = "The Shawshank Redemption",
            overview = "Imprisoned in the 1940s for the murder of his wife...",
            popularity = 31.7537,
            voteAverage = 8.712,
            voteCount = 25000,
            releaseDate = "1994-09-23",
            backdropUrl = "/zfbjgQEluSd9wiPTX4VzslorGfY.jpg",
            posterUrl = "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            genreIds = listOf(18, 80),
            originalLanguage = "en",
            adult = false,
            video = false
        )

        private val mockMovie2 = TopRatedMovie(
            id = 238,
            title = "The Godfather",
            originalTitle = "The Godfather",
            overview = "Spanning the years 1945 to 1955...",
            popularity = 45.123,
            voteAverage = 8.7,
            voteCount = 20000,
            releaseDate = "1972-03-14",
            backdropUrl = "/ejdD20cdHNFAYAN2DlqPToXKyzx.jpg",
            posterUrl = "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            genreIds = listOf(18, 80),
            originalLanguage = "en",
            adult = false,
            video = false
        )

        val mockTopRatedMovies = listOf(mockMovie1, mockMovie2)
    }
}
