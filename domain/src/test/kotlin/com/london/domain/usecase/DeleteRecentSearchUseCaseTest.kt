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

class DeleteRecentSearchUseCaseTest {
    lateinit var recentSearchRepository: RecentRepository<RecentSearch>
    lateinit var deleteRecentSearchUseCase: DeleteRecentSearchUseCase

    @Before
    fun setUp() {
        recentSearchRepository = mockk()
        deleteRecentSearchUseCase = DeleteRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should call the repository delete from recent search`() = runTest {
        //given
        val recentSearch = RecentSearch(1,"name",1)
        coEvery { recentSearchRepository.insert(recentSearch) } just Runs
        //when
        deleteRecentSearchUseCase.invoke(recentSearch)
        //then
        coVerify(exactly = 1) { recentSearchRepository.delete(recentSearch) }
    }

}