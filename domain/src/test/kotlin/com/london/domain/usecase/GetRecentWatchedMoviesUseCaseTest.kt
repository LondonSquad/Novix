package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.repository.RecentWatchedRepository
import com.london.domain.usecase.recent.watched.GetRecentWatchedMoviesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRecentWatchedMoviesUseCaseTest {
    private lateinit var recentWatchedRepository: RecentWatchedRepository
    private lateinit var getRecentWatchedMoviesUseCase: GetRecentWatchedMoviesUseCase
    @Before
    fun setUp() {
        recentWatchedRepository = mockk()
        getRecentWatchedMoviesUseCase = GetRecentWatchedMoviesUseCase(recentWatchedRepository)
    }
    @Test
    fun `invoke should return list of Movie when recentWatchedRepository returns list`() = runTest {
        // Given
        coEvery { recentWatchedRepository.getAllRecentWatchedMovies() } returns movieList
        // When
        val result = getRecentWatchedMoviesUseCase.invoke()
        // Then
        assertThat(result).isEqualTo(movieList)
    }
    @Test
    fun `invoke should return empty list when recentWatchedRepository returns empty list`() = runTest {
        // Given
        coEvery { recentWatchedRepository.getAllRecentWatchedMovies() } returns emptyList()
        // When
        val result = getRecentWatchedMoviesUseCase.invoke()
        // Then
        assertThat(result).isEmpty()
    }
    @Test
    fun `invoke should throw exception when recentWatchedRepository throws exception`() = runTest {
        // Given
        coEvery { recentWatchedRepository.getAllRecentWatchedMovies() } throws Exception()
        // When
        val result = runCatching { getRecentWatchedMoviesUseCase.invoke() }
        // Then
        assert(result.isFailure)
    }
    @Test
    fun `invoke should return limited list of Movie when limit is provided`()= runTest {
        // Given
        coEvery { recentWatchedRepository.getAllRecentWatchedMovies() } returns movieList
        // When
        val result = getRecentWatchedMoviesUseCase.invoke(limit = 2)
        // Then
        assertThat(result).hasSize(2)
    }

    companion object{
        private   val movie = Movie(
            id = 1,
            name = "movie1",
            posterUrl = "none",
            releaseYear = 1,
            rating = 1,
            genreIds = listOf(1, 2, 3)
        )
        private  val movieList = listOf(movie, movie, movie)
    }
}