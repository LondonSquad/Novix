package com.london.data.datasource.local.recent.watched

import com.google.common.truth.Truth.assertThat
import com.london.data.local.database.dao.recent.watched.movie.RecentWatchedMoviesDao
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.source.recent.watched.RecentWatchedMoviesDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecentWatchedMoviesDataSourceTest {
    private lateinit var recentWatchedMoviesDao: RecentWatchedMoviesDao
    private lateinit var recentWatchedMoviesDataSource: RecentWatchedMoviesDataSource

    @Before
    fun setUp() {
        recentWatchedMoviesDao = mockk()
        recentWatchedMoviesDataSource = RecentWatchedMoviesDataSource(recentWatchedMoviesDao)
    }

    @Test
    fun `getAll should return list of RecentWatchedTvShowLocal when dao returns list`() = runTest {
        // Given
        coEvery { recentWatchedMoviesDao.getAll() } returns flowOf(recentWatchedMovieLocalList)
        // When
        val result = recentWatchedMoviesDataSource.getAll().single()
        // Then
        assertThat(result).isEqualTo(recentWatchedMovieLocalList)
    }

    @Test
    fun `getAll should return empty list when dao returns empty list`() = runTest {
        // Given
        coEvery { recentWatchedMoviesDao.getAll() } returns flowOf(emptyList())
        // When
        val result = recentWatchedMoviesDataSource.getAll().single()
        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `insert should call dao insert`() = runTest {
        // Given
        coEvery { recentWatchedMoviesDao.insert(recentWatchedMovieLocal) } just Runs
        // When
        recentWatchedMoviesDataSource.insert(recentWatchedMovieLocal)
        // Then
        coVerify(exactly = 1) { recentWatchedMoviesDao.insert(recentWatchedMovieLocal) }
    }

    @Test
    fun `getAll should return empty list when dao throws exception`() = runTest {
        // Given
        coEvery { recentWatchedMoviesDao.getAll() } returns flow { throw Exception("DAO error") }
        // When
        val result = recentWatchedMoviesDataSource.getAll().single()
        // Then
        assertThat(result).isEmpty()
    }

    companion object {
        val recentWatchedMovieLocal = mockk<RecentWatchedMovieLocal>()
        val recentWatchedMovieLocalList =
            listOf(recentWatchedMovieLocal, recentWatchedMovieLocal, recentWatchedMovieLocal)
    }
}