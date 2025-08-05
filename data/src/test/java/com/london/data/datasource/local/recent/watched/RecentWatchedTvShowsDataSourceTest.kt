package com.london.data.datasource.local.recent.watched

import com.google.common.truth.Truth.assertThat
import com.london.data.local.database.dao.recent.watched.tvshow.RecentWatchedTvShowsDao
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.local.source.recent.watched.RecentWatchedTvShowsDataSource
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
import kotlin.test.Test

class RecentWatchedTvShowsDataSourceTest {
    private lateinit var recentWatchedTvShowsDao: RecentWatchedTvShowsDao
    private lateinit var recentWatchedTvShowsDataSource: RecentWatchedTvShowsDataSource

    @Before
    fun setUp() {
        recentWatchedTvShowsDao = mockk()
        recentWatchedTvShowsDataSource = RecentWatchedTvShowsDataSource(recentWatchedTvShowsDao)
    }

    @Test
    fun `getAll should return list of RecentWatchedTvShowLocal when dao returns list`() = runTest {
        // Given
        coEvery { recentWatchedTvShowsDao.getAll() } returns flowOf(recentWatchedTvShowLocalList)
        // When
        val result = recentWatchedTvShowsDataSource.getAll().single()
        // Then
        assertThat(result).isEqualTo(recentWatchedTvShowLocalList)
    }

    @Test
    fun `getAll should return empty list when dao returns empty list`() = runTest {
        // Given
        coEvery { recentWatchedTvShowsDao.getAll() } returns flowOf(emptyList())
        // When
        val result = recentWatchedTvShowsDataSource.getAll().single()
        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `insert should call dao insert`() = runTest {
        // Given
        coEvery { recentWatchedTvShowsDao.insert(recentWatchedTvShowLocal) } just Runs
        // When
        recentWatchedTvShowsDataSource.insert(recentWatchedTvShowLocal)
        // Then
        coVerify(exactly = 1) { recentWatchedTvShowsDao.insert(recentWatchedTvShowLocal) }
    }

    @Test
    fun `getAll should return empty list when dao throws exception`() = runTest {
        // Given
        coEvery { recentWatchedTvShowsDao.getAll() } returns flow { throw Exception("DAO error") }
        // When
        val result = recentWatchedTvShowsDataSource.getAll().single()
        // Then
        assertThat(result).isEmpty()
    }

    companion object {
        val recentWatchedTvShowLocal = mockk<RecentWatchedTvShowLocal>()
        val recentWatchedTvShowLocalList =
            listOf(recentWatchedTvShowLocal, recentWatchedTvShowLocal, recentWatchedTvShowLocal)
    }
}