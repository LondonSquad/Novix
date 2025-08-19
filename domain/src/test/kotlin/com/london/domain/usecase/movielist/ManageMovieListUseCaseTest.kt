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

class ManageMovieListUseCaseTest {

    private lateinit var customMovieListRepository: CustomMovieListRepository
    private lateinit var manageMovieListUseCase: ManageMovieListUseCase

    @Before
    fun setUp() {
        customMovieListRepository = mockk()
        manageMovieListUseCase = ManageMovieListUseCase(customMovieListRepository)
    }

    @Test
    fun `createMovieList should return true when movieListRepository returns true`() =
        runTest {
            // Given
            coEvery { customMovieListRepository.createMovieList(LIST_NAME) } returns true
            // When
            val result = manageMovieListUseCase.createMovieList(LIST_NAME)
            // Then
            assertThat(result).isTrue()
        }

    @Test
    fun `createMovieList should return false when movieListRepository returns false`() = runTest {
        // Given
        coEvery { customMovieListRepository.createMovieList(LIST_NAME) } returns false
        // When
        val result = manageMovieListUseCase.createMovieList(LIST_NAME)
        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `createMovieList should throw exception when movieListRepository throws exception`() =
        runTest {
            // Given
            coEvery { customMovieListRepository.createMovieList(LIST_NAME) } throws Exception()
            // When // Then
            assertThrows<Exception> {
                manageMovieListUseCase.createMovieList(LIST_NAME)
            }
        }

    @Test
    fun `deleteMovieList should return true when movieListRepository returns true`() = runTest {
        // Given
        coEvery { customMovieListRepository.deleteMovieList(LIST_ID) } returns true
        // When
        val result = manageMovieListUseCase.deleteMovieList(LIST_ID)
        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `deleteMovieList should return false when movieListRepository returns false`() = runTest {
        // Given
        coEvery { customMovieListRepository.deleteMovieList(LIST_ID) } returns false
        // When
        val result = manageMovieListUseCase.deleteMovieList(LIST_ID)
        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `deleteMovieList should throw exception when movieListRepository throws exception`() =
        runTest {
            // Given
            coEvery { customMovieListRepository.deleteMovieList(LIST_ID) } throws Exception()
            // When // Then
            assertThrows<Exception> {
                manageMovieListUseCase.deleteMovieList(LIST_ID)
            }
        }

    @Test
    fun `addMovieToList should return true when addMovieToList on repository returns true`() = runTest {
        //Given
        coEvery { customMovieListRepository.addMovieToList(any(), any()) } returns true
        //When
        val result = customMovieListRepository.addMovieToList(LIST_ID, MOVIE_ID)
        //Then
        assertThat(result).isTrue()
    }

    @Test
    fun `addMovieToList should return false when addMovieToList on repository returns false`() = runTest {
        //Given
        coEvery { customMovieListRepository.addMovieToList(any(), any()) } returns false
        //When
        val result = customMovieListRepository.addMovieToList(LIST_ID, MOVIE_ID)
        //Then
        assertThat(result).isFalse()
    }

    @Test
    fun `addMovieToList should throw exception when addMovieToList on repository throws exception`() =
        runTest {
            //Given
            coEvery { customMovieListRepository.addMovieToList(any(), any()) } throws Exception()
            //When //Then
            assertThrows<Exception> {
                customMovieListRepository.addMovieToList(LIST_ID, MOVIE_ID)
            }
        }

    @Test
    fun `getAllListedMovie throws exception when repository throws exception`() = runTest {
        //Given
        coEvery { customMovieListRepository.getAllListedMovieIds() } throws Exception()

        //When //Then
        assertThrows<Exception> {
            customMovieListRepository.getAllListedMovieIds()
        }
    }

    @Test
    fun `invoke should return movie list of movies from repository`() = runTest {
        //Given
        coEvery { customMovieListRepository.getMovieListDetails(any(), pageNumber = 1) } returns movieListDetails
        //When
        val result = customMovieListRepository.getMovieListDetails(LIST_ID, pageNumber = 1)
        //Then
        assertThat(result).isEqualTo(movieListDetails)
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        //Given
        coEvery { customMovieListRepository.getMovieListDetails(any(), pageNumber = 1) } throws Exception()
        //When //Then
        assertThrows<Exception> {
            customMovieListRepository.getMovieListDetails(LIST_ID, pageNumber = 1)
        }
    }

    @Test
    fun `invoke should return true when removeMovieFromList on repository returns true`() =
        runTest {
            //Given
            coEvery { customMovieListRepository.removeMovieFromList(any(), any()) } returns true
            //When
            val result = customMovieListRepository.removeMovieFromList(
                listId = LIST_ID,
                movieId = MOVIE_ID
            )
            //Then
            assertThat(result).isTrue()
        }

    @Test
    fun `invoke should return false when removeMovieFromList on repository returns false`() =
        runTest {
            //Given
            coEvery { customMovieListRepository.removeMovieFromList(any(), any()) } returns false
            //When
            val result = customMovieListRepository.removeMovieFromList(
                listId = LIST_ID,
                movieId = MOVIE_ID
            )
            //Then
            assertThat(result).isFalse()
        }

    @Test
    fun `invoke should throw exception when removeMovieFromList on repository throws exception`() =
        runTest {
            //Given
            coEvery {
                customMovieListRepository.removeMovieFromList(
                    any(),
                    any()
                )
            } throws Exception()
            //When //Then
            assertThrows<Exception> {
                customMovieListRepository.removeMovieFromList(
                    listId = LIST_ID,
                    movieId = MOVIE_ID
                )
            }
        }

    private companion object {
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

        const val LIST_ID = 10
        const val MOVIE_ID = 20
        const val LIST_NAME = "Movie List"
    }
}