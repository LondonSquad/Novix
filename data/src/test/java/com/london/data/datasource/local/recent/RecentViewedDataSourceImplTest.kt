package com.london.data.datasource.local.recent

import com.google.common.truth.Truth.assertThat
import com.london.data.local.database.dao.recent.viewed.RecentViewedDao
import com.london.data.local.model.recent.viewed.RecentViewedLocal
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
    private lateinit var dataSource: com.london.data.local.source.recent.RecentViewedDataSourceImpl

    @Before
    fun setUp() {
        recentViewedDao = mockk()
        dataSource = com.london.data.local.source.recent.RecentViewedDataSourceImpl(recentViewedDao)
    }

    @Test
    fun `when insert is called should delegate to DAO`() = runTest {
        coEvery { recentViewedDao.insert(Mock_Recent_Viewed_Local) } just Runs

        dataSource.insert(Mock_Recent_Viewed_Local)

        coVerify(exactly = 1) { recentViewedDao.insert(Mock_Recent_Viewed_Local) }
    }

    @Test
    fun `when clearOlderThanTen is called should delegate to DAO`() = runTest {
        coEvery { recentViewedDao.clearOlderThanTen() } just Runs

        dataSource.clearOlderThanTen()

        coVerify(exactly = 1) { recentViewedDao.clearOlderThanTen() }
    }

    @Test
    fun `when getAll is called should return result from DAO`() = runTest {
        coEvery { recentViewedDao.getAll() } returns listOf(Mock_Recent_Viewed_Local)

        val result = dataSource.getAll()

        assertThat(result).isEqualTo(listOf(Mock_Recent_Viewed_Local))
    }

    @Test
    fun `when getAll throws should return empty list`() = runTest {
        coEvery { recentViewedDao.getAll() } throws Exception()

        val result = dataSource.getAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `when getRecentTen is called should return result from DAO`() = runTest {
        coEvery { recentViewedDao.getRecentTen() } returns listOf(Mock_Recent_Viewed_Local)

        val result = dataSource.getRecentTen()

        assertThat(result).isEqualTo(listOf(Mock_Recent_Viewed_Local))
    }

    @Test
    fun `when getRecentTen throws should return empty list`() = runTest {
        coEvery { recentViewedDao.getRecentTen() } throws Exception()

        val result = dataSource.getRecentTen()

        assertThat(result).isEmpty()
    }

    @Test
    fun `when insertAndKeepLastTen is called should delegate to DAO`() = runTest {
        coEvery { recentViewedDao.insertAndKeepLastTen(Mock_Recent_Viewed_Local) } just Runs

        dataSource.insertAndKeepLastTen(Mock_Recent_Viewed_Local)

        coVerify(exactly = 1) { recentViewedDao.insertAndKeepLastTen(Mock_Recent_Viewed_Local) }
    }

    @Test
    fun `when clearAll is called should delegate to DAO`() = runTest {
        coEvery { recentViewedDao.clearAll() } just Runs

        dataSource.clearAll()

        coVerify(exactly = 1) { recentViewedDao.clearAll() }
    }

    companion object {
        val Mock_Recent_Viewed_Local = mockk<RecentViewedLocal>()
    }
}