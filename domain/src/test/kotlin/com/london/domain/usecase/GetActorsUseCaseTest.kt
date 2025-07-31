package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.error.ActorSearchFailedException
import com.london.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetActorsUseCaseTest {
    lateinit var searchRepository: SearchRepository
    lateinit var getActorsUseCase: GetActorsUseCase

    @Before
    fun setUp() {
        searchRepository = mockk()
        getActorsUseCase = GetActorsUseCase(searchRepository)
    }

    @Test
    fun `should return PagedFetchResponse when SearchRepository returns PagedFetchResponse`() = runTest {
        //given
        coEvery { searchRepository.searchForActors(NAME, PAGE_NUMBER) } returns pagedFetchResponse
        //when
        val result = getActorsUseCase(NAME, PAGE_NUMBER)
        //then
        assertThat(result).isEqualTo(pagedFetchResponse)
    }

    @Test
    fun `should throw ActorSearchFailedException when SearchRepository throws ActorSearchFailedException`() = runTest {
        //given
        coEvery {
            searchRepository.searchForActors(
                NAME,
                PAGE_NUMBER
            )
        } throws ActorSearchFailedException()
        //when //then
        assertThrows<ActorSearchFailedException> {
            getActorsUseCase(NAME, PAGE_NUMBER)
        }
    }


    private companion object {
      private  const val NAME = "Tom"
      private  const val PAGE_NUMBER = 1
      private  val ACTOR = Actor(
            id = 1,
            name = "Tom Holland",
            profilePictureUrl = "",
            characterName = ""
        )
     private  val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1,
            items = listOf(ACTOR),
            totalPages = 1,
            totalItems = 1
        )
    }
}