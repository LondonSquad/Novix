package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.error.MovieSearchFailedException
import com.london.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesUseCaseTest {
    lateinit var searchRepository: SearchRepository
    lateinit var getMoviesUseCase: GetMoviesUseCase

    @Before
    fun setUp() {
        searchRepository = mockk()
        getMoviesUseCase = GetMoviesUseCase(searchRepository)
    }

    @Test
    fun `should return a paged fetch response of movies when repository successfully fetches movies`() = runTest {
        //given
        coEvery { searchRepository.searchForMovies(NAME, PAGE_NUMBER) } returns pagedFetchResponse
        //when
        val result = getMoviesUseCase(NAME, PAGE_NUMBER)
        //then
        assertThat(result).isEqualTo(pagedFetchResponse)
    }

    @Test
    fun `should throw MovieSearchFailedException when repository throws an exception during movie search`() = runTest {
        //given
        coEvery { searchRepository.searchForMovies(NAME, PAGE_NUMBER) } throws MovieSearchFailedException()
        //when //then
        assertThrows<MovieSearchFailedException> {
            getMoviesUseCase(NAME, PAGE_NUMBER)
        }
    }


    private companion object {
       private const val NAME = "Movie"
        private const val PAGE_NUMBER = 1
        private  val movie = Movie(
            id = 1,
            name = NAME,
            posterUrl = "",
            releaseYear = 2024,
            rating = 8,
            genreIds = listOf(1, 2, 3)
        )
        val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1,
            items = listOf(movie),
            totalPages = 1,
            totalItems = 1
        )
    }
}