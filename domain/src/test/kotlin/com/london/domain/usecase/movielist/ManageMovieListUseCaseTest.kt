package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
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

    private companion object {
        const val LIST_ID = 10u
        const val LIST_NAME = "Movie List"
    }
}