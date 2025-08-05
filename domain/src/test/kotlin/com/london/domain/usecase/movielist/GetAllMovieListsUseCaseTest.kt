package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetAllMovieListsUseCaseTest {

    private lateinit var customMovieListRepository: CustomMovieListRepository
    private lateinit var getAllMovieListsUseCase: GetAllMovieListsUseCase

    @Before
    fun setUp() {
        customMovieListRepository = mockk()
        getAllMovieListsUseCase = GetAllMovieListsUseCase(customMovieListRepository)
    }

    @Test
    fun `invoke should return movie lists from repository`() = runTest {
        // Given
        coEvery { customMovieListRepository.getMovieLists() } returns movieLists
        // When
        val result = getAllMovieListsUseCase.invoke()
        // Then
        assertThat(result).isEqualTo(movieLists)
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        // Given
        coEvery { customMovieListRepository.getMovieLists() } throws Exception()
        // When // Then
        assertThrows<Exception> {
            getAllMovieListsUseCase.invoke()
        }
    }


   private companion object {
        const val LIST_ID = 10u
        const val MOVIE_ID = 20u
        val movieLists = PagedFetchResponse(
            currentPage = 1,
            totalPages = 1,
            totalItems = 1,
            items = listOf(
                MovieList(
                    id = LIST_ID,
                    name = "list1",
                    moviesCount = 1u
                ),
                MovieList(
                    id = LIST_ID,
                    name = "list1",
                    moviesCount = 1u
                )
            )
        )
    }
}