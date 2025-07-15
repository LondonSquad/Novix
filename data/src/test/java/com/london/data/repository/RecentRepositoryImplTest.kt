package com.london.data.repository

import com.london.data.datasource.local.model.RecentSearch
import com.london.data.datasource.local.recentsearch.RecentSearchDataSource
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
import kotlin.test.Test

class RecentRepositoryImplTest {
    lateinit var recentSearchDataSource: RecentSearchDataSource
    lateinit var recentRepository: RecentRepository
    @Before
    fun setUp() {
        recentSearchDataSource = mockk()
        recentRepository = RecentRepositoryImpl(recentSearchDataSource)
    }


    @Test
    fun `should map data source results to string list when getAll is called`() = runTest {
        // given
        val fakeEntity = mockk<RecentSearch>()
        coEvery { recentSearchDataSource.getAll() } returns listOf(fakeEntity)
        every { fakeEntity.toStringQuery() } returns "query"
        // when
        val result = recentRepository.getAll()
        // then
        assert(result == listOf("query"))
    }

    @Test
    fun `should call clearAll on data source`() = runTest {
        // given
        coEvery { recentSearchDataSource.clearAll() } just Runs
        // when
        recentRepository.clearAll()
        // then
        coVerify(exactly = 1) { recentSearchDataSource.clearAll() }
    }

    @Test
    fun `should call insertAndKeepLastTen with any RecentSearch`() = runTest {
        // given
        coEvery { recentSearchDataSource.insertAndKeepLastTen(any()) } just Runs
        // when
        recentRepository.insert("query")
        // then
        coVerify(exactly = 1) {
            recentSearchDataSource.insertAndKeepLastTen(ofType<RecentSearch>())
        }
    }
}

