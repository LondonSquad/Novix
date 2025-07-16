package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class IncrementGenreInterestUseCaseTest {

    private lateinit var repository: SearchRepository
    private lateinit var useCase: IncrementGenreInterestUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = IncrementGenreInterestUseCase(repository)
    }

    @Test
    fun `invoke should call repository to increment genre interest`() = runTest {
        val genreId = 28
        val mediaType = "tv"

        useCase.invoke(genreId, mediaType)

        coVerify { repository.incrementGenreInterest(genreId, mediaType) }
    }

}
