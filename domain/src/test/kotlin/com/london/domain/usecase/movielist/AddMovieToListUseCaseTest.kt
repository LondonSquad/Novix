package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.MovieListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class AddMovieToListUseCaseTest {

    private lateinit var movieListRepository: MovieListRepository
    private lateinit var addMovieToListUseCase: AddMovieToListUseCase

    @Before
    fun setUp() {
        movieListRepository = mockk()
        addMovieToListUseCase = AddMovieToListUseCase(movieListRepository)
    }

    @Test
    fun `invoke should return true when addMovieToList on repository returns true`() = runTest {
        //Given
        coEvery { movieListRepository.addMovieToList(any(), any()) } returns true
        //When
        val result = addMovieToListUseCase.invoke(LIST_ID, MOVIE_ID)
        //Then
        assertThat(result).isTrue()
    }

    @Test
    fun `invoke should return false when addMovieToList on repository returns false`() = runTest {
        //Given
        coEvery { movieListRepository.addMovieToList(any(), any()) } returns false
        //When
        val result = addMovieToListUseCase.invoke(LIST_ID, MOVIE_ID)
        //Then
        assertThat(result).isFalse()
    }

    @Test
    fun `invoke should throw exception when addMovieToList on repository throws exception`() =
        runTest {
            //Given
            coEvery { movieListRepository.addMovieToList(any(), any()) } throws Exception()
            //When //Then
            assertThrows<Exception> {
                addMovieToListUseCase.invoke(LIST_ID, MOVIE_ID)
            }
        }


    private companion object {
        const val LIST_ID = 10u
        const val MOVIE_ID = 20u
    }
}