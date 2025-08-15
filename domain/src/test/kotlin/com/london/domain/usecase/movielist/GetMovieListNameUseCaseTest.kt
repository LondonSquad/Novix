package com.london.domain.usecase.movielist

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.CustomMovieListRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMovieListNameUseCaseTest {

    private lateinit var getMovieListNameUseCase: GetMovieListNameUseCase
    private lateinit var customMovieListRepository: CustomMovieListRepository

    @Before
    fun setUp() {
        customMovieListRepository = mockk()
        getMovieListNameUseCase = GetMovieListNameUseCase(customMovieListRepository)
    }

    @Test
    fun `invoke should return movie list name when repository returns name`() = runTest {
        // Given
        val listId = 1
        val expectedName = "Movie List Name"
        coEvery { customMovieListRepository.getMovieListName(listId) } returns expectedName
        // When
        val result = getMovieListNameUseCase.invoke(listId)
        // Then
        assertThat(result).isEqualTo(expectedName)
    }

    @Test
    fun `invoke should throw exception when repository throws exception`() = runTest {
        // Given
        val listId = 1
        coEvery { customMovieListRepository.getMovieListName(listId) } throws Exception()
        // When // Then
        assertThrows<Exception> {
            getMovieListNameUseCase.invoke(listId)

        }
    }
}
