package com.london.domain.usecase.recent.search

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.repository.RecentRepository
import com.london.domain.usecase.search.recent.search.ManageRecentSearchUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ManageRecentSearchUseCaseTest {
    private lateinit var recentSearchRepository: RecentRepository<RecentSearch>
    private lateinit var manageRecentSearchUseCase: ManageRecentSearchUseCase

    @Before
    fun setUp() {
        recentSearchRepository = mockk()
        manageRecentSearchUseCase = ManageRecentSearchUseCase(recentSearchRepository)
    }

    // region InsertRecentSearch
    @Test
    fun `should call the repository add to recent search`() = runTest {
        // given
        val recentSearch = RecentSearch(1, "name", 1)
        coEvery { recentSearchRepository.insert(recentSearch) } just Runs

        // when
        manageRecentSearchUseCase.addToRecentSearch(recentSearch)

        // then
        coVerify(exactly = 1) { recentSearchRepository.insert(recentSearch) }
    }

    @Test
    fun `should handle empty query string`() = runTest {
        // given
        val recentSearch = RecentSearch(2, "", System.currentTimeMillis())
        coEvery { recentSearchRepository.insert(recentSearch) } just Runs

        // when
        manageRecentSearchUseCase.addToRecentSearch(recentSearch)

        // then
        coVerify(exactly = 1) { recentSearchRepository.insert(recentSearch) }
    }
    // endregion

    // region DeleteRecentSearch
    @Test
    fun `should call the repository delete from recent search`() = runTest {
        //given
        val recentSearch = RecentSearch(1,"name",1)
        coEvery { recentSearchRepository.delete(recentSearch) } just Runs
        //when
        manageRecentSearchUseCase.deleteRecentSearch(recentSearch)
        //then
        coVerify(exactly = 1) { recentSearchRepository.delete(recentSearch) }
    }
    // endregion

    // region GetRecentSearch
    @Test
    fun `should return a list of string when repository return a list of string`() = runTest {
        //given
        val recentSearch = RecentSearch(1,"name",1)
        coEvery { recentSearchRepository.getAll() } returns listOf(recentSearch)
        //when
        val result = manageRecentSearchUseCase.getRecentSearch()
        //then
        assertThat(result).isEqualTo(listOf(recentSearch))
    }
    // endregion

    // region ClearRecentSearch
    @Test
    fun `should call the repository clear all`() = runTest {
        //given
        coEvery { recentSearchRepository.clearAll() } just Runs
        //when
        manageRecentSearchUseCase.clearRecentSearch()
        //then
        coVerify(exactly = 1) { recentSearchRepository.clearAll() }
    }
    // endregion

}