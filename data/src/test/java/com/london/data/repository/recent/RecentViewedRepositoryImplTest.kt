package com.london.data.repository.recent

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.recent.MediaTypeLocal
import com.london.data.local.model.recent.viewed.RecentViewedLocal
import com.london.data.local.source.recent.RecentDataSource
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecentViewedRepositoryImplTest {
    lateinit var recentViewedLocalDataSource: RecentDataSource<RecentViewedLocal>
    lateinit var recentViewedRepository: RecentRepository<RecentViewed>

    @Before
    fun setUp() {
        recentViewedLocalDataSource = mockk()
        recentViewedRepository = RecentViewedRepositoryImpl(recentViewedLocalDataSource)
    }

    @Test
    fun `getAll should map data source results to domain when is called`() = runTest {
        // given
        coEvery { recentViewedLocalDataSource.getAll() } returns listOf(local)
        // when
        val result = recentViewedRepository.getAll()
        // then
        assertThat(result).isEqualTo(listOf(entity))
    }

    @Test
    fun `should call clearAll on data source`() = runTest {
        // given
        coEvery { recentViewedLocalDataSource.clearAll() } just Runs
        // when
        recentViewedRepository.clearAll()
        // then
        coVerify(exactly = 1) { recentViewedLocalDataSource.clearAll() }
    }

    @Test
    fun `insert should call insertAndKeepLastTen with any RecentViewed`() = runTest {
        // given
        coEvery { recentViewedLocalDataSource.insertAndKeepLastTen(any()) } just Runs
        // when
        recentViewedRepository.insert(entity)
        // then
        coVerify(exactly = 1) {
            recentViewedLocalDataSource.insertAndKeepLastTen(local)
        }
    }

    @Test
    fun `delete should call delete with mapped RecentViewedLocal`() = runTest {
        // given
        coEvery { recentViewedLocalDataSource.delete(any()) } just Runs
        // when
        recentViewedRepository.delete(entity)
        // then
        coVerify(exactly = 1) {
            recentViewedLocalDataSource.delete(local)
        }
    }
    companion object {
        val entity = RecentViewed(
            id = 1, imageUrl = "", type = MediaType.Movie, viewDate = 1234567L
        )
        val local = RecentViewedLocal(
            id = 1, imageUrl = "", type = MediaTypeLocal.Movie, viewDate = 1234567L
        )
    }
}