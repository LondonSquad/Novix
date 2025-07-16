package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class GetGenreInterestCountsUseCaseTest {

    private lateinit var repository: SearchRepository
    private lateinit var useCase: GetGenreInterestCountsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetGenreInterestCountsUseCase(repository)
    }


    @Test
    fun `invoke should return list of genre interest counts`(): Unit = runTest {
        val mediaType = "movie"
        val expected = listOf(1 to 5, 2 to 3)

        coEvery { repository.getGenreInterestCounts(mediaType) } returns expected

        val result = useCase.invoke(mediaType)

        assertThat(result).isEqualTo(expected)
        coVerify { repository.getGenreInterestCounts(mediaType) }
    }
}
