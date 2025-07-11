package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.TvShowSearchFailedException
import com.london.domain.entity.TvShow
import com.london.domain.repo.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetTvShowsUseCaseTest {
    lateinit var searchRepository: SearchRepository
    lateinit var getTvShowsUseCase: GetTvShowsUseCase


    @Before
    fun setUp() {
        searchRepository = mockk()
        getTvShowsUseCase = GetTvShowsUseCase(searchRepository)
    }


    @Test
    fun `should return a list of tv shows when repository return a list of actors`() = runTest {
        //given
        coEvery { searchRepository.searchForTvShows(NAME, LANGUAGE) } returns listOf(TV_SHOW)
        //when
        val result = getTvShowsUseCase(NAME, LANGUAGE)
        //then
        assertThat(result).isEqualTo(listOf(TV_SHOW))
    }

    @Test
    fun `should throw an exception when repository throws an exception`() = runTest {
        //given
        coEvery { searchRepository.searchForTvShows(NAME, LANGUAGE) } throws TvShowSearchFailedException()
        //when //then
        assertThrows<TvShowSearchFailedException> {
            getTvShowsUseCase(NAME, LANGUAGE)
        }
    }


    private companion object {
        const val NAME = "Tv Tv"
        const val LANGUAGE = "en-US"
        val TV_SHOW = TvShow(
            id = 1,
            name = NAME,
            posterPicture = ""
        )
    }
}