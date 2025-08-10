package com.london.domain.usecase.recent.viewed

import com.google.common.truth.Truth.assertThat
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

class ManageRecentViewedUseCaseTest {
    private lateinit var recentViewedRepository: RecentRepository<RecentViewed>
    private lateinit var manageRecentViewedUseCase: ManageRecentViewedUseCase

    @Before
    fun setUp() {
        recentViewedRepository = mockk()
        manageRecentViewedUseCase = ManageRecentViewedUseCase(recentViewedRepository)
    }

    // region Add
    @Test
    fun `should call the repository add to recent Viewed and send item`() = runTest {
        //given
        val recentViewedMok = mockk<RecentViewed>()
        coEvery { recentViewedRepository.insert(recentViewedMok) } just Runs
        //when
        manageRecentViewedUseCase.addToRecentViewed(recentViewedMok)
        //then
        coVerify(exactly = 1) { recentViewedRepository.insert(recentViewedMok) }
    }
    // endregion

    // region Clear
    @Test
    fun `should call the repository clear all`() = runTest {
        //given
        coEvery { recentViewedRepository.clearAll() } just Runs
        //when
        manageRecentViewedUseCase.clearRecentViewed()
        //then
        coVerify(exactly = 1) { recentViewedRepository.clearAll() }
    }
    // endregion

    // region Get
    @Test
    fun `should return a list of recent viewed when repository return a list of recent viewed`() = runTest {
        //given
        val recentViewedMok = mockk<RecentViewed>()
        coEvery { recentViewedRepository.getAll() } returns listOf(recentViewedMok)
        //when
        val result = manageRecentViewedUseCase.getRecentViewed()
        //then
        assertThat(result).isEqualTo(listOf(recentViewedMok))
    }
    // endregion
}