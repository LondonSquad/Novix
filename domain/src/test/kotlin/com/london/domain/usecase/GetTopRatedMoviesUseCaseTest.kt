package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.repository.toprated.TopRatedMovieRepository
import com.london.domain.usecase.toprated.GetTopRatedMoviesUseCase
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
            voteAverage = 8.712,
            releaseDate = "1994-09-23",
            posterUrl = "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            genreIds = listOf(18, 80),
        )

        private val mockMovie2 = TopRatedMovie(
            id = 238,
            title = "The Godfather",
            voteAverage = 8.7, releaseDate = "1972-03-14",
            posterUrl = "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            genreIds = listOf(18, 80),
        )

        val mockTopRatedMovies = listOf(mockMovie1, mockMovie2)
    }
}