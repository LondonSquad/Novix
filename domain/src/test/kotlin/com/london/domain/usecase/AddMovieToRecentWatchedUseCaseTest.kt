package com.london.domain.usecase

import com.london.domain.entity.Movie
import com.london.domain.repository.RecentWatchedRepository
import com.london.domain.usecase.recent.watched.AddMovieToRecentWatchedUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddMovieToRecentWatchedUseCaseTest {
    private lateinit var recentWatchedRepository: RecentWatchedRepository
    private lateinit var addMovieToRecentWatchedUseCase: AddMovieToRecentWatchedUseCase
    @Before
    fun setUp() {
        recentWatchedRepository = mockk()
        addMovieToRecentWatchedUseCase = AddMovieToRecentWatchedUseCase(recentWatchedRepository)
    }
    @Test
    fun `invoke should call recentWatchedRepository insertMovie`() = runTest {
        // Given
        coEvery { recentWatchedRepository.insertMovie(movie) } just Runs
        // When
        addMovieToRecentWatchedUseCase.invoke(movie)
        // Then
        coVerify(exactly = 1) { recentWatchedRepository.insertMovie(movie) }
    }
    @Test
    fun `invoke should throw exception when recentWatchedRepository throws exception`() = runTest {
        // Given
        coEvery { recentWatchedRepository.insertMovie(movie) } throws Exception()
        // When
        val result = runCatching { addMovieToRecentWatchedUseCase.invoke(movie) }
        // Then
        assert(result.isFailure)
    }

    companion object{
       private val movie = Movie(
            id = 1,
            name = "movie1",
            posterUrl = "none",
            releaseYear = 1,
            rating = 1,
            genreIds = listOf(1, 2, 3)
        )
    }
}