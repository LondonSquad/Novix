package com.london.domain.usecase

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
    lateinit var recentRepository: RecentRepository
    lateinit var clearRecentSearchUseCase: ClearRecentSearchUseCase

    @Before
    fun setUp() {
        recentRepository = mockk()
        clearRecentSearchUseCase = ClearRecentSearchUseCase(recentRepository)
    }

    @Test
    fun `should call the repository clear all`() = runTest {
        //given
        coEvery { recentRepository.clearAll() } just Runs
        //when
        clearRecentSearchUseCase.invoke()
        //then
        coVerify(exactly = 1) { recentRepository.clearAll() }
    }
}