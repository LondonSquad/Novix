package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.SearchRepository
import com.london.domain.usecase.search.ManageSearchUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetActorsUseCaseTest {
    lateinit var repository: SearchRepository
    lateinit var manageSearchUseCase: ManageSearchUseCase

    @Before
    fun setUp() {
        repository = mockk()

        manageSearchUseCase = ManageSearchUseCase(
            repository = repository
        )
    }

    @Test
    fun `should return PagedFetchResponse when SearchRepository returns PagedFetchResponse`() =
        runTest {
            //given
            coEvery {
                repository.searchForActors(
                    NAME,
                    PAGE_NUMBER
                )
            } returns pagedFetchResponse
            //when
            val result = manageSearchUseCase.searchForActors(NAME, PAGE_NUMBER)
            //then
            assertThat(result).isEqualTo(pagedFetchResponse)
        }


    private companion object {
        private const val NAME = "Tom"
        private const val PAGE_NUMBER = 1
        private val ACTOR = Actor(
            id = 1,
            name = "Tom Holland",
            profilePictureUrl = "",
            characterName = ""
        )
        private val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1,
            items = listOf(ACTOR),
            totalPages = 1,
            totalItems = 1
        )
    }
}