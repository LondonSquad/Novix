package com.london.domain.usecase

import com.london.domain.entity.recent.RecentSearch
import com.london.domain.repository.RecentRepository
import com.london.domain.usecase.recent.search.AddToRecentSearchUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddToRecentSearchUseCaseTest {
    lateinit var recentSearchRepository: RecentRepository<RecentSearch>
    lateinit var addToRecentSearchUseCase: AddToRecentSearchUseCase

    @Before
    fun setUp() {
        recentSearchRepository = mockk()
        addToRecentSearchUseCase = AddToRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should call the repository add to recent search`() = runTest {
        // given
        val recentSearch = RecentSearch(1, "name", 1)
        coEvery { recentSearchRepository.insert(recentSearch) } just Runs

        // when
        addToRecentSearchUseCase.invoke(recentSearch)

        // then
        coVerify(exactly = 1) { recentSearchRepository.insert(recentSearch) }
    }

    @Test
    fun `should handle empty query string`() = runTest {
        // given
        val recentSearch = RecentSearch(2, "", System.currentTimeMillis())
        coEvery { recentSearchRepository.insert(recentSearch) } just Runs

        // when
        addToRecentSearchUseCase.invoke(recentSearch)

        // then
        coVerify(exactly = 1) { recentSearchRepository.insert(recentSearch) }
    }
}