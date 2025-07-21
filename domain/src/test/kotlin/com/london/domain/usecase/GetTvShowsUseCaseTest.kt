package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.TvShowSearchFailedException
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.repository.SearchRepository
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
    fun `should return paged fetch response when repository returns paged fetch response`() = runTest {
        //given
        coEvery {
            searchRepository.searchForTvShows(
                NAME,
                PAGE_NUMBER
            )
        } returns pagedFetchResponse
        //when
        val result = getTvShowsUseCase(
            NAME,
            PAGE_NUMBER
        )
        //then
        assertThat(result).isEqualTo(pagedFetchResponse)
    }

    @Test
    fun `should throw TvShowSearchFailedException when repository throws TvShowSearchFailedException`() = runTest {
        //given
        coEvery {
            searchRepository.searchForTvShows(
                NAME,
                PAGE_NUMBER
            )
        } throws TvShowSearchFailedException()
        //when //then
        assertThrows<TvShowSearchFailedException> {
            getTvShowsUseCase(
                NAME,
                PAGE_NUMBER
            )
        }
    }

    private companion object {
        const val NAME = "Tv Tv"
        const val PAGE_NUMBER = 1
        val TV_SHOW = TvShow(
            id = 1,
            name = NAME,
            posterPicture = "",
            releaseYear = 2024,
            rating = 8,
            genres = listOf(1, 2, 3)
        )
        val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1,
            items = listOf(TV_SHOW),
            totalPages = 1,
            totalItems = 1
        )
    }
}