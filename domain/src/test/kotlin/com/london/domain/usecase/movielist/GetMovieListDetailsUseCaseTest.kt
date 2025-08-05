package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMovieListDetailsUseCaseTest {

    private lateinit var listRepository: CustomMovieListRepository
    private lateinit var getMovieListDetailsUseCase: GetMovieListDetailsUseCase

    @Before
    fun setUp() {
        listRepository = mockk()
        getMovieListDetailsUseCase = GetMovieListDetailsUseCase(listRepository)
    }

    @Test
    fun `invoke should return movie list of movies from repository`() = runTest {
        //Given
        coEvery { listRepository.getMovieListDetails(any()) } returns movieListDetails
        //When
        val result = getMovieListDetailsUseCase.invoke(LIST_ID)
        //Then
        assertThat(result).isEqualTo(movieListDetails)
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        //Given
        coEvery { listRepository.getMovieListDetails(any()) } throws Exception()
        //When //Then
        assertThrows<Exception> {
            getMovieListDetailsUseCase.invoke(LIST_ID)
        }
    }

    private companion object {
        const val LIST_ID = 10u
        val movieListDetails = PagedFetchResponse(
            currentPage = 1,
            totalPages = 1,
            totalItems = 1,
            items = listOf(
                Movie(
                    id = 1,
                    name = "movie1",
                    posterUrl = "none",
                    releaseYear = 1,
                    rating = 1,
                    genreIds = listOf(1, 2, 3)
                ),
            )
        )
    }
}