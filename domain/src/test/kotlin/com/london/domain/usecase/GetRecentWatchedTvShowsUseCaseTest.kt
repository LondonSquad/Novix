package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.TvShow
import com.london.domain.repository.RecentWatchedRepository
import com.london.domain.usecase.recent.watched.GetRecentWatchedTvShowsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRecentWatchedTvShowsUseCaseTest {
    private lateinit var recentWatchedRepository: RecentWatchedRepository
    private lateinit var getRecentWatchedTvShowsUseCase: GetRecentWatchedTvShowsUseCase

    @Before
    fun setUp() {
        recentWatchedRepository = mockk()
        getRecentWatchedTvShowsUseCase = GetRecentWatchedTvShowsUseCase(recentWatchedRepository)
    }

    @Test
    fun `invoke should return list of tv show when recentWatchedRepository returns list`() =
        runTest {
            // Given
            coEvery { recentWatchedRepository.getAllRecentWatchedTvShows() } returns flow {
                emit(
                    tvShowList
                )
            }
            // When
            val result = getRecentWatchedTvShowsUseCase.getAll().single()
            // Then
            assertThat(result).isEqualTo(tvShowList)
        }

    @Test
    fun `invoke should return empty list when recentWatchedRepository returns empty list`() =
        runTest {
            // Given
            coEvery { recentWatchedRepository.getAllRecentWatchedTvShows() } returns flow {
                emit(
                    emptyList()
                )
            }
            // When
            val result = getRecentWatchedTvShowsUseCase.getAll().single()
            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `invoke should throw exception when recentWatchedRepository throws exception`() = runTest {
        // Given
        coEvery { recentWatchedRepository.getAllRecentWatchedTvShows() } throws Exception()
        // When
        val result = runCatching { getRecentWatchedTvShowsUseCase.getAll().single() }
        // Then
        assert(result.isFailure)
    }

    @Test
    fun `invoke should return limited list of Movie when limit is provided`() = runTest {
        // Given
        coEvery { recentWatchedRepository.getAllRecentWatchedTvShows() } returns flow {
            emit(
                tvShowList
            )
        }
        // When
        val result = getRecentWatchedTvShowsUseCase.getAll(limit = 2).single()
        // Then
        assertThat(result).hasSize(2)
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
        val tvShowList = listOf(tvShow, tvShow, tvShow)
    }
}