package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.ActorSearchFailedException
import com.london.domain.entity.Actor
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
    fun `should return a list of actors when repository return a list of actors`() = runTest {
        //given
        coEvery { searchRepository.searchForActors(NAME, LANGUAGE) } returns listOf(Actor)
        //when
        val result = getActorsUseCase(NAME, LANGUAGE)
        //then
        assertThat(result).isEqualTo(listOf(Actor))
    }

    @Test
    fun `should throw an exception when repository throws an exception`() = runTest {
        //given
        coEvery {
            searchRepository.searchForActors(
                NAME,
                LANGUAGE
            )
        } throws ActorSearchFailedException()
        //when //then
        assertThrows<ActorSearchFailedException> {
            getActorsUseCase(NAME, LANGUAGE)
        }
    }


    private companion object {
        const val NAME = "Tom"
        const val LANGUAGE = "en-US"
        val Actor = Actor(
            id = 1, name = "Tom Holland", profilePicture = ""
        )
    }
}