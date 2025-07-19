package com.london.data.repository.recent

import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.data.datasource.local.recent.RecentDataSource
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.repository.RecentRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecentRepositoryImplTest {
    lateinit var recentSearchLocalDataSource: RecentDataSource<RecentSearchLocal>
    lateinit var recentSearchRepository: RecentRepository<RecentSearch>
    @Before
    fun setUp() {
        recentSearchLocalDataSource = mockk()
        recentSearchRepository = RecentSearchRepositoryImpl(recentSearchLocalDataSource)
    }


    @Test
    fun `should map data source results to string list when getAll is called`() = runTest {
        // given
        val fakeEntity = RecentSearchLocal(1,"name",1)
        coEvery { recentSearchLocalDataSource.getAll() } returns listOf(fakeEntity)

        // when
        val result = recentSearchRepository.getAll()
        // then
        assert(result == listOf(RecentSearch(1,"name",1)))
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
        recentSearchRepository.insert(RecentSearch(1,"name",1))
        // then
        coVerify(exactly = 1) {
            recentSearchLocalDataSource.insertAndKeepLastTen(ofType<RecentSearchLocal>())
        }
    }

    @Test
    fun `should call delete on data source with mapped RecentSearchLocal`() = runTest {
        // given
        coEvery { recentSearchLocalDataSource.delete(any()) } just Runs

        // when
        recentSearchRepository.delete(RecentSearch(1, "name", 1))

        // then
        coVerify(exactly = 1) {
            recentSearchLocalDataSource.delete(ofType<RecentSearchLocal>())
        }
    }
}