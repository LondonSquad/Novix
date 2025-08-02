package com.london.domain.usecase

import com.london.domain.repository.AddMovieRatingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddMovieRatingByIdUseCaseTest {

    private lateinit var repository: AddMovieRatingRepository
    private lateinit var useCase: AddMovieRatingByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = AddMovieRatingByIdUseCase(repository)
    }

    @Test
    fun `given valid id and rating when invoked then returns true`() = runTest {
        // Given
        val movieId = 456
        val rating = 7.5
        coEvery { repository.addMovieRatingById(movieId, rating.toInt()) } returns true

        // When
        val result = useCase(movieId, rating.toInt())

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { repository.addMovieRatingById(movieId, rating.toInt()) }
    }

    @Test
    fun `given valid id and rating when repository returns false then useCase returns false`() =
        runTest {
            // Given
            val movieId = 789
            val rating = 4.0
            coEvery { repository.addMovieRatingById(movieId, rating.toInt()) } returns false

            // When
            val result = useCase(movieId, rating.toInt())

            // Then
            assertFalse(result)
            coVerify(exactly = 1) { repository.addMovieRatingById(movieId, rating.toInt()) }
        }
}
