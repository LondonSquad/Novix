package com.london.domain.usecase

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

class ClearRecentViewedUseCaseTest {
    lateinit var recentViewedRepository: RecentRepository<RecentViewed>
    lateinit var clearRecentViewedUseCase: ClearRecentViewedUseCase

    @Before
    fun setUp() {
        recentViewedRepository = mockk()
        clearRecentViewedUseCase = ClearRecentViewedUseCase(recentViewedRepository)
    }

    @Test
    fun `should call the repository clear all`() = runTest {
        //given
        coEvery { recentViewedRepository.clearAll() } just Runs
        //when
        clearRecentViewedUseCase.invoke()
        //then
        coVerify(exactly = 1) { recentViewedRepository.clearAll() }
    }
}