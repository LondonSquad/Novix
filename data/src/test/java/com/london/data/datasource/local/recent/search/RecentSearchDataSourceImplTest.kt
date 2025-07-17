package com.london.data.datasource.local.recent.search

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.local.dao.recent.search.RecentSearchDao
import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.data.datasource.local.recent.RecentSearchDataSourceImpl
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
    fun `insert should call insert on dao`() = runTest {
        // given
        coEvery { recentSearchDao.insert(Mock_Recent_Search_Local) } just Runs
        // when
        dataSource.insert(Mock_Recent_Search_Local)
        // then
        coVerify(exactly = 1) { recentSearchDao.insert(Mock_Recent_Search_Local) }
    }

    @Test
    fun `clearOlderThanTen should call clearOlderThanTen on dao`() = runTest {
        // given
        coEvery { recentSearchDao.clearOlderThanTen() } just Runs
        // when
        dataSource.clearOlderThanTen()
        // then
        coVerify(exactly = 1) { recentSearchDao.clearOlderThanTen() }
    }

    @Test
    fun `getAll should return dao getAll result`() = runTest {
        // given
        coEvery { recentSearchDao.getAll() } returns listOf(Mock_Recent_Search_Local)
        // when
        val result = dataSource.getAll()
        // then
        assertThat(result).isEqualTo(listOf(Mock_Recent_Search_Local))
    }

    @Test
    fun `getAll should return empty list when getAll throws`() = runTest {
        // given
        coEvery { recentSearchDao.getAll() } throws Exception()
        // when
        val result = dataSource.getAll()
        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getRecentTen should return dao getRecentTen result`() = runTest {
        // given
        coEvery { recentSearchDao.getRecentTen() } returns listOf(Mock_Recent_Search_Local)
        // when
        val result = dataSource.getRecentTen()
        // then
        assertThat(result).isEqualTo(listOf(Mock_Recent_Search_Local))
    }

    @Test
    fun `getRecentTen should return empty list when getRecentTen throws`() = runTest {
        // given
        coEvery { recentSearchDao.getRecentTen() } throws Exception()
        // when
        val result = dataSource.getRecentTen()
        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `insertAndKeepLastTen should call insertAndKeepLastTen on dao`() = runTest {
        // given
        coEvery { recentSearchDao.insertAndKeepLastTen(Mock_Recent_Search_Local) } just Runs
        // when
        dataSource.insertAndKeepLastTen(Mock_Recent_Search_Local)
        // then
        coVerify(exactly = 1) { recentSearchDao.insertAndKeepLastTen(Mock_Recent_Search_Local) }
    }

    @Test
    fun `clearAll should call clearAll on dao`() = runTest {
        // given
        coEvery { recentSearchDao.clearAll() } just Runs
        // when
        dataSource.clearAll()
        // then
        coVerify(exactly = 1) { recentSearchDao.clearAll() }
    }

    companion object {
        val Mock_Recent_Search_Local = mockk<RecentSearchLocal>()
    }
}
