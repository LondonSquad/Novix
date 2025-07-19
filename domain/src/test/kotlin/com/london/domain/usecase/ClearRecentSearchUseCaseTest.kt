package com.london.domain.usecase

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

class ClearRecentSearchUseCaseTest {
    lateinit var recentSearchRepository: RecentRepository<RecentSearch>
    lateinit var clearRecentSearchUseCase: ClearRecentSearchUseCase

    @Before
    fun setUp() {
        recentSearchRepository = mockk()
        clearRecentSearchUseCase = ClearRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should call the repository clear all`() = runTest {
        //given
        coEvery { recentSearchRepository.clearAll() } just Runs
        //when
        clearRecentSearchUseCase.invoke()
        //then
        coVerify(exactly = 1) { recentSearchRepository.clearAll() }
    }
}