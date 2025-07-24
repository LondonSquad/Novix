package com.london.domain.usecase

import com.london.domain.entity.TvShow
import com.london.domain.repository.RecentWatchedRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddTvShowToRecentWatchedUseCaseTest {
    private lateinit var recentWatchedRepository: RecentWatchedRepository
    private lateinit var addTvShowToRecentWatchedUseCase: AddTvShowToRecentWatchedUseCase

    @Before
    fun setUp() {
        recentWatchedRepository = mockk()
        addTvShowToRecentWatchedUseCase = AddTvShowToRecentWatchedUseCase(recentWatchedRepository)
    }

    @Test
    fun `invoke should call recentWatchedRepository insertMovie`() = runTest {
        // Given
        coEvery { recentWatchedRepository.insertTvShow(tvShow) } just Runs
        // When
        addTvShowToRecentWatchedUseCase.invoke(tvShow)
        // Then
        coVerify(exactly = 1) { recentWatchedRepository.insertTvShow(tvShow) }
    }

    @Test
    fun `invoke should throw exception when recentWatchedRepository throws exception`() = runTest {
        // Given
        coEvery { recentWatchedRepository.insertTvShow(tvShow) } throws Exception()
        // When
        val result = runCatching { addTvShowToRecentWatchedUseCase.invoke(tvShow) }
        // Then
        assert(result.isFailure)
    }

    companion object {
        val tvShow = TvShow(
            id = 1,
            name = "tv1",
            posterPicture = "none",
            releaseYear = 1,
            rating = 1,
            genres = listOf(1, 2, 3),
        )
    }
}
