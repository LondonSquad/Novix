package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.repository.CustomMovieListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetAllListedMoviesTest {

    private lateinit var customMovieListRepository: CustomMovieListRepository
    private lateinit var getAllListedMovies: GetAllListedMovies

    @Before
    fun setUp() {
        customMovieListRepository = mockk()
        getAllListedMovies = GetAllListedMovies(customMovieListRepository)
    }

    @Test
    fun `invoke should return all listed movie IDs`() = runTest {
        //Given
        val movieIds = listOf(1, 2, 3, 4)
        coEvery { customMovieListRepository.getAllListedMovieIds() } returns movieIds

        //When
        val result = getAllListedMovies.invoke()

        //Then
        val expectedResult = setOf(1, 2, 3, 4)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `invoke throws exception when repository throws exception`() = runTest {
        //Given
        coEvery { customMovieListRepository.getAllListedMovieIds() } throws Exception()

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
                    id = 1,
                    name = "list1",
                    moviesCount = 1
                ),
                MovieList(
                    id = 2,
                    name = "list2",
                    moviesCount = 1
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
                    genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
                ),
                Movie(
                    id = 2,
                    name = "movie2",
                    posterUrl = "none",
                    releaseYear = 1,
                    rating = 1,
                    genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
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
                    genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
                ),
                Movie(
                    id = 4,
                    name = "movie4",
                    posterUrl = "none",
                    releaseYear = 1,
                    rating = 1,
                    genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
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
                genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
            ),
            Movie(
                id = 2,
                name = "movie2",
                posterUrl = "none",
                releaseYear = 1,
                rating = 1,
                genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
            ),
            Movie(
                id = 3,
                name = "movie3",
                posterUrl = "none",
                releaseYear = 1,
                rating = 1,
                genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
            ),
            Movie(
                id = 4,
                name = "movie4",
                posterUrl = "none",
                releaseYear = 1,
                rating = 1,
                genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION, MovieGenre.ACTION)
            )
        )
    }
}