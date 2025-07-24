package com.london.domain.usecase

import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import com.london.domain.usecase.recent.viewed.AddToRecentViewedUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddToRecentViewedUseCaseTest {
    lateinit var recentViewedRepository: RecentRepository<RecentViewed>
    lateinit var addToRecentViewedUseCase: AddToRecentViewedUseCase

    @Before
    fun setUp() {
        recentViewedRepository = mockk()
        addToRecentViewedUseCase = AddToRecentViewedUseCase(recentViewedRepository)
    }

    @Test
    fun `should call the repository add to recent Viewed and send item`() = runTest {
        //given
        val recentViewedMok = mockk<RecentViewed>()
        coEvery { recentViewedRepository.insert(recentViewedMok) } just Runs
        //when
        addToRecentViewedUseCase.invoke(recentViewedMok)
        //then
        coVerify(exactly = 1) { recentViewedRepository.insert(recentViewedMok) }
    }
}