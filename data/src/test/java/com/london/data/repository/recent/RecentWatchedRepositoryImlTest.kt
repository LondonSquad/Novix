package com.london.data.repository.recent

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.local.source.recent.watched.RecentWatchedDataSource
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class RecentWatchedRepositoryImlTest {
    private lateinit var recentWatchedMoviesDataSource: RecentWatchedDataSource<RecentWatchedMovieLocal>
    private lateinit var recentWatchedTvShowsDataSource: RecentWatchedDataSource<RecentWatchedTvShowLocal>
    private lateinit var recentWatchedRepositoryIml: RecentWatchedRepositoryIml

    @Before
    fun setUp() {
        recentWatchedMoviesDataSource = mockk()
        recentWatchedTvShowsDataSource = mockk()
        recentWatchedRepositoryIml = RecentWatchedRepositoryIml(
            recentWatchedMoviesDataSource, recentWatchedTvShowsDataSource
        )
    }

    @Test
    fun `getAllRecentWatchedMovies should return list of Movie when recentWatchedMoviesDataSource returns list`() =
        runTest {
            // Given
            coEvery { recentWatchedMoviesDataSource.getAll() } returns recentWatchedMovieLocalList
            // When
            val result = recentWatchedRepositoryIml.getAllRecentWatchedMovies()
            // Then
            assertThat(result).isEqualTo(movieList)
        }

    @Test
    fun `getAllRecentWatchedMovies should return empty list when recentWatchedMoviesDataSource returns empty list`() =
        runTest {
            // Given
            coEvery { recentWatchedMoviesDataSource.getAll() } returns emptyList()
            // When
            val result = recentWatchedRepositoryIml.getAllRecentWatchedMovies()
            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `getAllRecentWatchedTvShows should return list of TvShow when recentWatchedTvShowsDataSource returns list`() =
        runTest {
            // Given
            coEvery { recentWatchedTvShowsDataSource.getAll() } returns recentWatchedTvShowLocalList
            // When
            val result = recentWatchedRepositoryIml.getAllRecentWatchedTvShows()
            // Then
            assertThat(result).isEqualTo(tvShowList)
        }

    @Test
    fun `getAllRecentWatchedTvShows should return empty list when recentWatchedTvShowsDataSource returns empty list`() =
        runTest {
            // Given
            coEvery { recentWatchedTvShowsDataSource.getAll() } returns emptyList()
            // When
            val result = recentWatchedRepositoryIml.getAllRecentWatchedTvShows()
            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `insertMovie should call recentWatchedMoviesDataSource insert`() = runTest {
        // Given
        coEvery { recentWatchedMoviesDataSource.insert(any()) } just Runs
        // When
        recentWatchedRepositoryIml.insertMovie(movie)
        // Then
        coVerify(exactly = 1) { recentWatchedMoviesDataSource.insert(any()) }
    }

    @Test
    fun `insertTvShow should call recentWatchedTvShowsDataSource insert`() = runTest {
        // Given
        coEvery { recentWatchedTvShowsDataSource.insert(any()) } just Runs
        // When
        recentWatchedRepositoryIml.insertTvShow(tvShow)
        // Then
        coVerify(exactly = 1) { recentWatchedTvShowsDataSource.insert(any()) }
    }

    companion object {
        val recentWatchedMovieLocal = RecentWatchedMovieLocal(
            id = 1,
            name = "movie1",
            posterPictureUrl = "none",
            releaseYear = 1,
            rating = 1,
            genreIds = listOf(1, 2, 3),
            watchedAt = 1111
        )
        val recentWatchedTvShowLocal = RecentWatchedTvShowLocal(
            id = 1,
            name = "tv1",
            posterPictureUrl = "none",
            releaseYear = 1,
            rating = 1,
            genres = listOf(1, 2, 3),
            watchedAt = 111111
        )
        val recentWatchedMovieLocalList =
            listOf(recentWatchedMovieLocal, recentWatchedMovieLocal, recentWatchedMovieLocal)
        val recentWatchedTvShowLocalList =
            listOf(recentWatchedTvShowLocal, recentWatchedTvShowLocal, recentWatchedTvShowLocal)
        val movie = Movie(
            id = 1,
            name = "movie1",
            posterUrl = "none",
            releaseYear = 1,
            rating = 1,
            genreIds = listOf(1, 2, 3)
        )
        val tvShow = TvShow(
            id = 1,
            name = "tv1",
            posterPicture = "none",
            releaseYear = 1,
            rating = 1,
            genres = listOf(1, 2, 3)
        )
        val movieList = listOf(movie, movie, movie)
        val tvShowList = listOf(tvShow, tvShow, tvShow)
    }
}