package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.shared.PagedFetchResponse
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
        coEvery { listRepository.getMovieListDetails(any(), pageNumber = 1) } returns movieListDetails
        //When
        val result = getMovieListDetailsUseCase.getMovieListDetails(LIST_ID, pageNumber = 1)
        //Then
        assertThat(result).isEqualTo(movieListDetails)
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        //Given
        coEvery { listRepository.getMovieListDetails(any(), pageNumber = 1) } throws Exception()
        //When //Then
        assertThrows<Exception> {
            getMovieListDetailsUseCase.getMovieListDetails(LIST_ID, pageNumber = 1)
        }
    }

    private companion object {
        const val LIST_ID = 10
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
                    genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
                ),
            )
        )
    }
}