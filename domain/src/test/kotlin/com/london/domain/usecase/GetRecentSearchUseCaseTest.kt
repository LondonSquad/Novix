package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.RecentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRecentSearchUseCaseTest {
    lateinit var recentRepository: RecentRepository
    lateinit var getRecentSearchUseCase: GetRecentSearchUseCase
    @Before
    fun setUp() {
        recentRepository = mockk()
        getRecentSearchUseCase = GetRecentSearchUseCase(recentRepository)
    }

    @Test
    fun `should return a list of string when repository return a list of string`() = runTest {
        //given
        coEvery { recentRepository.getAll() } returns listOf("aa","bb")
        //when
        val result = getRecentSearchUseCase.invoke()
        //then
        assertThat(result).isEqualTo(listOf("aa","bb"))
    }
}