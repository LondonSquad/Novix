package com.london.data.repository.recent

import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.data.datasource.local.recent.RecentDataSource
import com.london.data.mapper.recent.toStringQuery
import com.london.domain.repository.RecentRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecentRepositoryImplTest {
    lateinit var recentSearchLocalDataSource: RecentDataSource<RecentSearchLocal>
    lateinit var recentSearchRepository: RecentRepository<String>
    @Before
    fun setUp() {
        recentSearchLocalDataSource = mockk()
        recentSearchRepository = RecentSearchRepositoryImpl(recentSearchLocalDataSource)
    }


    @Test
    fun `should map data source results to string list when getAll is called`() = runTest {
        // given
        val fakeEntity = mockk<RecentSearchLocal>()
        coEvery { recentSearchLocalDataSource.getAll() } returns listOf(fakeEntity)
        every { fakeEntity.toStringQuery() } returns "query"
        // when
        val result = recentSearchRepository.getAll()
        // then
        assert(result == listOf("query"))
    }

    @Test
    fun `should call clearAll on data source`() = runTest {
        // given
        coEvery { recentSearchLocalDataSource.clearAll() } just Runs
        // when
        recentSearchRepository.clearAll()
        // then
        coVerify(exactly = 1) { recentSearchLocalDataSource.clearAll() }
    }

    @Test
    fun `should call insertAndKeepLastTen with any RecentSearch`() = runTest {
        // given
        coEvery { recentSearchLocalDataSource.insertAndKeepLastTen(any()) } just Runs
        // when
        recentSearchRepository.insert("query")
        // then
        coVerify(exactly = 1) {
            recentSearchLocalDataSource.insertAndKeepLastTen(ofType<RecentSearchLocal>())
        }
    }
}