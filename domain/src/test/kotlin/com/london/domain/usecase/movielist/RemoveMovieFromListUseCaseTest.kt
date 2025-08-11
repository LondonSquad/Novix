package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.CustomMovieListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class RemoveMovieFromListUseCaseTest {
    private lateinit var customMovieListRepository: CustomMovieListRepository
    private lateinit var manageMovieListUseCase: ManageMovieListUseCase

    @Before
    fun setUp() {
        customMovieListRepository = mockk()
        manageMovieListUseCase = ManageMovieListUseCase(customMovieListRepository)
    }

    @Test
    fun `invoke should return true when removeMovieFromList on repository returns true`() =
        runTest {
            //Given
            coEvery { customMovieListRepository.removeMovieFromList(any(), any()) } returns true
            //When
            val result = manageMovieListUseCase.removeMovieFromList(LIST_ID, MOVIE_ID)
            //Then
            assertThat(result).isTrue()
        }

    @Test
    fun `invoke should return false when removeMovieFromList on repository returns false`() =
        runTest {
            //Given
            coEvery { customMovieListRepository.removeMovieFromList(any(), any()) } returns false
            //When
            val result = manageMovieListUseCase.removeMovieFromList(LIST_ID, MOVIE_ID)
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
                manageMovieListUseCase.removeMovieFromList(LIST_ID, MOVIE_ID)
            }
        }


    private companion object {
        const val LIST_ID = 10u
        const val MOVIE_ID = 20u
    }
}