package com.london.data.datasource.local.recent

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.local.dao.recent.viewed.RecentViewedDao
import com.london.data.datasource.local.model.recent.RecentViewedLocal
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class RecentViewedDataSourceImplTest {

    private lateinit var recentViewedDao: RecentViewedDao
    private lateinit var dataSource: RecentViewedDataSourceImpl

    @Before
    fun setUp() {
        recentViewedDao = mockk()
        dataSource = RecentViewedDataSourceImpl(recentViewedDao)
    }

    @Test
    fun `insert should call insert on dao`() = runTest {
        // given
        coEvery { recentViewedDao.insert(Mock_Recent_Viewed_Local) } just Runs
        // when
        dataSource.insert(Mock_Recent_Viewed_Local)
        // then
        coVerify(exactly = 1) { recentViewedDao.insert(Mock_Recent_Viewed_Local) }
    }

    @Test
    fun `clearOlderThanTen should call clearOlderThanTen on dao`() = runTest {
        // given
        coEvery { recentViewedDao.clearOlderThanTen() } just Runs
        // when
        dataSource.clearOlderThanTen()
        // then
        coVerify(exactly = 1) { recentViewedDao.clearOlderThanTen() }
    }

    @Test
    fun `getAll should return dao getAll result`() = runTest {
        // given
        coEvery { recentViewedDao.getAll() } returns listOf(Mock_Recent_Viewed_Local)
        // when
        val result = dataSource.getAll()
        // then
        assertThat(result).isEqualTo(listOf(Mock_Recent_Viewed_Local))
    }

    @Test
    fun `getAll should return empty list when getAll throws`() = runTest {
        // given
        coEvery { recentViewedDao.getAll() } throws Exception()
        // when
        val result = dataSource.getAll()
        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getRecentTen should return dao getRecentTen result`() = runTest {
        // given
        coEvery { recentViewedDao.getRecentTen() } returns listOf(Mock_Recent_Viewed_Local)
        // when
        val result = dataSource.getRecentTen()
        // then
        assertThat(result).isEqualTo(listOf(Mock_Recent_Viewed_Local))
    }

    @Test
    fun `getRecentTen should return empty list when getRecentTen throws`() = runTest {
        // given
        coEvery { recentViewedDao.getRecentTen() } throws Exception()
        // when
        val result = dataSource.getRecentTen()
        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `insertAndKeepLastTen should call insertAndKeepLastTen on dao`() = runTest {
        // given
        coEvery { recentViewedDao.insertAndKeepLastTen(Mock_Recent_Viewed_Local) } just Runs
        // when
        dataSource.insertAndKeepLastTen(Mock_Recent_Viewed_Local)
        // then
        coVerify(exactly = 1) { recentViewedDao.insertAndKeepLastTen(Mock_Recent_Viewed_Local) }
    }

    @Test
    fun `clearAll should call clearAll on dao`() = runTest {
        // given
        coEvery { recentViewedDao.clearAll() } just Runs
        // when
        dataSource.clearAll()
        // then
        coVerify(exactly = 1) { recentViewedDao.clearAll() }
    }

    companion object {
        val Mock_Recent_Viewed_Local = mockk<RecentViewedLocal>()
    }
}