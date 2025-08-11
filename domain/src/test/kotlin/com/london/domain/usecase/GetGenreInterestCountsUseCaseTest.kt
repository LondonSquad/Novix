package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.SearchRepository
import com.london.domain.usecase.search.ManageSearchUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class GetGenreInterestCountsUseCaseTest {

    private lateinit var repository: SearchRepository
    private lateinit var searchUseCase: ManageSearchUseCase

    @Before
    fun setUp() {
        repository = mockk()
        searchUseCase = ManageSearchUseCase(repository)
    }


    @Test
    fun `invoke should return list of genre interest counts`(): Unit = runTest {
        val mediaType = "movie"
        val expected = listOf(1 to 5, 2 to 3)

        coEvery { repository.getGenreInterestCounts(mediaType) } returns expected

        val result = searchUseCase.getGenreInterestCounts(mediaType)

        assertThat(result).isEqualTo(expected)
        coVerify { repository.getGenreInterestCounts(mediaType) }
    }
}
