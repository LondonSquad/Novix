package com.london.data.datasource.local.recent.search

import com.google.common.truth.Truth.assertThat
import com.london.data.local.database.dao.recent.search.RecentSearchDao
import com.london.data.local.model.recent.search.RecentSearchLocal
import com.london.data.local.source.recent.RecentSearchDataSourceImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecentSearchDataSourceImplTest {

    private lateinit var recentSearchDao: RecentSearchDao
    private lateinit var dataSource: RecentSearchDataSourceImpl

    @Before
    fun setUp() {
        recentSearchDao = mockk()
        dataSource = RecentSearchDataSourceImpl(recentSearchDao)
    }

    @Test
    fun `when inserting search item should call dao insert`() = runTest {
        coEvery { recentSearchDao.insert(Mock_Recent_Search_Local) } just Runs

        dataSource.insert(Mock_Recent_Search_Local)

        coVerify(exactly = 1) { recentSearchDao.insert(Mock_Recent_Search_Local) }
    }

    @Test
    fun `when clearing older items should call dao clearOlderThanTen`() = runTest {
        coEvery { recentSearchDao.clearOlderThanTen() } just Runs

        dataSource.clearOlderThanTen()

        coVerify(exactly = 1) { recentSearchDao.clearOlderThanTen() }
    }

    @Test
    fun `when getting all searches should return dao result`() = runTest {
        coEvery { recentSearchDao.getAll() } returns listOf(Mock_Recent_Search_Local)

        val result = dataSource.getAll()

        assertThat(result).isEqualTo(listOf(Mock_Recent_Search_Local))
    }

    @Test
    fun `when getting all searches and dao throws should return empty list`() = runTest {
        coEvery { recentSearchDao.getAll() } throws Exception()

        val result = dataSource.getAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `when getting recent 10 searches should return dao result`() = runTest {
        coEvery { recentSearchDao.getRecentTen() } returns listOf(Mock_Recent_Search_Local)

        val result = dataSource.getRecentTen()

        assertThat(result).isEqualTo(listOf(Mock_Recent_Search_Local))
    }

    @Test
    fun `when getting recent 10 searches and dao throws should return empty list`() = runTest {
        coEvery { recentSearchDao.getRecentTen() } throws Exception()

        val result = dataSource.getRecentTen()

        assertThat(result).isEmpty()
    }

    @Test
    fun `when inserting and keeping last 10 searches should call dao insertAndKeepLastTen`() =
        runTest {
        coEvery { recentSearchDao.insertAndKeepLastTen(Mock_Recent_Search_Local) } just Runs

            dataSource.insertAndKeepLastTen(Mock_Recent_Search_Local)

            coVerify(exactly = 1) { recentSearchDao.insertAndKeepLastTen(Mock_Recent_Search_Local) }
    }

    @Test
    fun `when clearing all searches should call dao clearAll`() = runTest {
        coEvery { recentSearchDao.clearAll() } just Runs

        dataSource.clearAll()

        coVerify(exactly = 1) { recentSearchDao.clearAll() }
    }


    companion object {
        val Mock_Recent_Search_Local = mockk<RecentSearchLocal>()
    }
}
