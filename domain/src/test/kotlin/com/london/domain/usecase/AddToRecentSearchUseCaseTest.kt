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

class AddToRecentSearchUseCaseTest {
    lateinit var recentSearchRepository: RecentRepository<String>
    lateinit var addToRecentSearchUseCase: AddToRecentSearchUseCase

    @Before
    fun setUp() {
        recentSearchRepository = mockk()
        addToRecentSearchUseCase = AddToRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should call the repository add to recent search`() = runTest {
        //given
        coEvery { recentSearchRepository.insert("name") } just Runs
        //when
        addToRecentSearchUseCase.invoke("name")
        //then
        coVerify(exactly = 1) { recentSearchRepository.insert("name") }
    }

}