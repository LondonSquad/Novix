package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
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
    fun `invoke should return true when addMovieToList on repository returns true`() = runTest {
        //Given
        coEvery { customMovieListRepository.addMovieToList(any(), any()) } returns true
        //When
        val result = manageMovieListUseCase.addMovieToList(LIST_ID, MOVIE_ID)
        //Then
        assertThat(result).isTrue()
    }

    @Test
    fun `invoke should return false when addMovieToList on repository returns false`() = runTest {
        //Given
        coEvery { customMovieListRepository.addMovieToList(any(), any()) } returns false
        //When
        val result = manageMovieListUseCase.addMovieToList(LIST_ID, MOVIE_ID)
        //Then
        assertThat(result).isFalse()
    }

    @Test
    fun `invoke should throw exception when addMovieToList on repository throws exception`() =
        runTest {
            //Given
            coEvery { customMovieListRepository.addMovieToList(any(), any()) } throws Exception()
            //When //Then
            assertThrows<Exception> {
                manageMovieListUseCase.addMovieToList(LIST_ID, MOVIE_ID)
            }
        }


    @Test
    fun `getMovieLists should return movie lists from repository`() = runTest {
        // Given
        coEvery { customMovieListRepository.getMovieLists(pageNumber = 1) } returns movieLists
        // When
        val result = manageMovieListUseCase.getMovieLists(pageNumber = 1)
        // Then
        assertThat(result).isEqualTo(movieLists)
    }

    @Test
    fun `getMovieLists should throw exception when repository throws exception`() = runTest {
        // Given
        coEvery { customMovieListRepository.getMovieLists(pageNumber = 1) } throws Exception()
        // When // Then
        assertThrows<Exception> {
            manageMovieListUseCase.getMovieLists(pageNumber = 1)
        }
    }


    @Test
    fun `invoke should return movie list name when repository returns name`() = runTest {
        // Given
        val listId = 1u
        val expectedName = "Movie List Name"
        coEvery { customMovieListRepository.getMovieListName(listId) } returns expectedName
        // When
        val result = manageMovieListUseCase.getMovieListName(listId)
        // Then
        assertThat(result).isEqualTo(expectedName)
    }

    @Test
    fun `getMovieListName should throw exception when repository throws exception`() = runTest {
        // Given
        val listId = 1u
        coEvery { customMovieListRepository.getMovieListName(listId) } throws Exception()
        // When // Then
        assertThrows<Exception> {
            manageMovieListUseCase.getMovieListName(listId)
        }
    }

    @Test
    fun `invoke should return true when removeMovieFromList on repository returns true`() =
        runTest {
            //Given
            coEvery { customMovieListRepository.removeMovieFromList(any(), any()) } returns true
            //When
            val result = manageMovieListUseCase.removeMovieFromList(
                LIST_ID,
                MOVIE_ID
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
            val result = manageMovieListUseCase.removeMovieFromList(
                LIST_ID,
                MOVIE_ID
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
                manageMovieListUseCase.removeMovieFromList(
                    LIST_ID,
                    MOVIE_ID
                )
            }
        }

    @Test
    fun `invoke should return movie list of movies from repository`() = runTest {
        //Given
        coEvery {
            customMovieListRepository.getMovieListDetails(
                any(),
                pageNumber = 1
            )
        } returns movieListDetails
        //When
        val result = manageMovieListUseCase.getMovieListDetails(LIST_ID, pageNumber = 1)
        //Then
        assertThat(result).isEqualTo(movieListDetails)
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        //Given
        coEvery {
            customMovieListRepository.getMovieListDetails(
                any(),
                pageNumber = 1
            )
        } throws Exception()
        //When //Then
        assertThrows<Exception> {
            manageMovieListUseCase.getMovieListDetails(LIST_ID, pageNumber = 1)
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
        val movieListDetails = PagedFetchResponse(
            currentPage = 1,
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
            ),
            totalPages = 1
        )
        const val LIST_NAME = "Movie List"
    }
}