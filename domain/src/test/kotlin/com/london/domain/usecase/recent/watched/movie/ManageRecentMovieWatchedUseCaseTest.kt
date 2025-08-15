package com.london.domain.usecase.recent.watched.movie

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Movie
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.repository.RecentWatchedRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ManageRecentMovieWatchedUseCaseTest{
    private lateinit var recentWatchedRepository: RecentWatchedRepository
    private lateinit var manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase

    @Before
    fun setUp() {
        recentWatchedRepository = mockk()
        manageRecentMovieWatchedUseCase = ManageRecentMovieWatchedUseCase(recentWatchedRepository)
    }

    // region AddMovieToRecentWatched
    @Test
    fun `addMovieToRecentWatched should call recentWatchedRepository insertMovie`() = runTest {
        // Given
        coEvery { recentWatchedRepository.insertMovie(movie) } just Runs
        // When
        manageRecentMovieWatchedUseCase.addMovieToRecentWatched(movie)
        // Then
        coVerify(exactly = 1) { recentWatchedRepository.insertMovie(movie) }
    }
    @Test
    fun `invoke should throw exception when recentWatchedRepository throws exception`() = runTest {
        // Given
        coEvery { recentWatchedRepository.insertMovie(movie) } throws Exception()
        // When
        val result = runCatching { manageRecentMovieWatchedUseCase.addMovieToRecentWatched(movie) }
        // Then
        assert(result.isFailure)
    }
    // endregion

    // region GetAllRecentWatched
    @Test
    fun `invoke should return list of Movie when recentWatchedRepository returns list`() = runTest {
        // Given
        coEvery { recentWatchedRepository.getAllRecentWatchedMovies() } returns flow {
            emit(movieList)
        }
        // When
        val result = manageRecentMovieWatchedUseCase.getAllWatchedMovies().single()
        // Then
        assertThat(result).isEqualTo(movieList)
    }

    @Test
    fun `invoke should return empty list when recentWatchedRepository returns empty list`() =
        runTest {
            // Given
            coEvery { recentWatchedRepository.getAllRecentWatchedMovies() } returns flow {
                emit(
                    emptyList()
                )
            }
            // When
            val result = manageRecentMovieWatchedUseCase.getAllWatchedMovies().single()
            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `invoke should return limited list of Movie when limit is provided`() = runTest {
        // Given
        coEvery { recentWatchedRepository.getAllRecentWatchedMovies() } returns flow {
            emit(
                movieList
            )
        }
        // When
        val result = manageRecentMovieWatchedUseCase.getAllWatchedMovies(limit = 2).single()
        // Then
        assertThat(result).hasSize(2)
    }
    //endregion

    companion object{
        private val movie = Movie(
            id = 1,
            name = "movie1",
            posterUrl = "none",
            releaseYear = 1,
            rating = 1,
            genres = listOf(MovieGenre.ACTION, MovieGenre.ACTION,MovieGenre.ACTION)
        )
        private val movieList = listOf(movie, movie, movie)
    }
}