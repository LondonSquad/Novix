package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRecentViewedUseCaseTest {
    lateinit var recentViewedRepository: RecentRepository<RecentViewed>
    lateinit var getRecentViewedUseCaseTest: GetRecentViewedUseCase
    @Before
    fun setUp() {
        recentViewedRepository = mockk()
        getRecentViewedUseCaseTest = GetRecentViewedUseCase(recentViewedRepository)
    }

    @Test
    fun `should return a list of recent viewed when repository return a list of recent viewed`() = runTest {
        //given
        val recentViewedMok = mockk<RecentViewed>()
        coEvery { recentViewedRepository.getAll() } returns listOf(recentViewedMok)
        //when
        val result = getRecentViewedUseCaseTest.invoke()
        //then
        assertThat(result).isEqualTo(listOf(recentViewedMok))
    }
}