package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.MovieListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetAllListedMoviesTest {

    private lateinit var movieListRepository: MovieListRepository
    private lateinit var getAllListedMovies: GetAllListedMovies

    @Before
    fun setUp() {
        movieListRepository = mockk()
        getAllListedMovies = GetAllListedMovies(movieListRepository)
    }

    @Test
    fun `invoke should return all listed movies`() = runTest {
        //Given
        coEvery { movieListRepository.getMovieLists() } returns movieLists
        coEvery { movieListRepository.getMovieListDetails(listId = 1u) } returns listDetails1
        coEvery { movieListRepository.getMovieListDetails(listId = 2u) } returns listDetails2
        //When
        val result = getAllListedMovies.invoke()
        //Then
        assertThat(result).isEqualTo(listedMovies)
    }

    @Test
    fun `invoke throws exception when movieListRepository throws exception`() = runTest {
        //Given
        coEvery { movieListRepository.getMovieLists() } throws Exception()
        //When //Then
        assertThrows<Exception> {
            getAllListedMovies.invoke()
        }
    }

    companion object {
        val movieLists = PagedFetchResponse<MovieList>(
            currentPage = 1,
            totalPages = 1,
            totalItems = 1,
            items = listOf(
                MovieList(
                    id = 1u,
                    name = "list1",
                    moviesCount = 1u
                ),
                MovieList(
                    id = 2u,
                    name = "list2",
                    moviesCount = 1u
                ),
            )
        )
        val listDetails1 = PagedFetchResponse<Movie>(
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
                Movie(
                    id = 2,
                    name = "movie2",
                    posterUrl = "none",
                    releaseYear = 1,
                    rating = 1,
                    genreIds = listOf(1, 6, 3)
                )
            )
        )
        val listDetails2 = PagedFetchResponse<Movie>(
            currentPage = 1,
            totalPages = 1,
            totalItems = 1,
            items = listOf(
                Movie(
                    id = 3,
                    name = "movie3",
                    posterUrl = "none",
                    releaseYear = 1,
                    rating = 1,
                    genreIds = listOf(1, 2, 3)
                ),
                Movie(
                    id = 4,
                    name = "movie4",
                    posterUrl = "none",
                    releaseYear = 1,
                    rating = 1,
                    genreIds = listOf(1, 6, 3)
                )
            )
        )
        val listedMovies = setOf(
            Movie(
                id = 1,
                name = "movie1",
                posterUrl = "none",
                releaseYear = 1,
                rating = 1,
                genreIds = listOf(1, 2, 3)
            ),
            Movie(
                id = 2,
                name = "movie2",
                posterUrl = "none",
                releaseYear = 1,
                rating = 1,
                genreIds = listOf(1, 6, 3)
            ),
            Movie(
                id = 3,
                name = "movie3",
                posterUrl = "none",
                releaseYear = 1,
                rating = 1,
                genreIds = listOf(1, 2, 3)
            ),
            Movie(
                id = 4,
                name = "movie4",
                posterUrl = "none",
                releaseYear = 1,
                rating = 1,
                genreIds = listOf(1, 6, 3)
            )
        )
    }
}