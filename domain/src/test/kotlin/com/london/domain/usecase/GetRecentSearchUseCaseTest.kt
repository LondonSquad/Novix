package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.repository.RecentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRecentSearchUseCaseTest {
    lateinit var recentSearchRepository: RecentRepository<RecentSearch>
    lateinit var getRecentSearchUseCase: GetRecentSearchUseCase
    @Before
    fun setUp() {
        recentSearchRepository = mockk()
        getRecentSearchUseCase = GetRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should return a list of string when repository return a list of string`() = runTest {
        //given
        val recentSearch = RecentSearch(1,"name",1)
        coEvery { recentSearchRepository.getAll() } returns listOf(recentSearch)
        //when
        val result = getRecentSearchUseCase.invoke()
        //then
        assertThat(result).isEqualTo(listOf(recentSearch))
    }
}