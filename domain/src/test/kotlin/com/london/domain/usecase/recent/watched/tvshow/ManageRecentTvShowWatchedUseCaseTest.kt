package com.london.domain.usecase.recent.watched.tvshow

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.repository.RecentWatchedRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ManageRecentTvShowWatchedUseCaseTest {
    private lateinit var recentWatchedRepository: RecentWatchedRepository
    private lateinit var manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase

    @Before
    fun setUp() {
        recentWatchedRepository = mockk()
        manageRecentTvShowWatchedUseCase = ManageRecentTvShowWatchedUseCase(recentWatchedRepository)
    }

    // region AddTvShowToRecentWatched
    @Test
    fun `invoke should call recentWatchedRepository insertMovie`() = runTest {
        // Given
        coEvery { recentWatchedRepository.insertTvShow(tvShow) } just Runs
        // When
        manageRecentTvShowWatchedUseCase.addTvShowToRecentWatched(tvShow)
        // Then
        coVerify(exactly = 1) { recentWatchedRepository.insertTvShow(tvShow) }
    }

    @Test
    fun `invoke should throw exception when recentWatchedRepository throws exception`() = runTest {
        // Given
        coEvery { recentWatchedRepository.insertTvShow(tvShow) } throws Exception()
        // When
        val result =
            runCatching { manageRecentTvShowWatchedUseCase.addTvShowToRecentWatched(tvShow) }
        // Then
        assert(result.isFailure)
    }
    // endregion

    // region GetTvShowRecentWatched
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
            val result = manageRecentTvShowWatchedUseCase.getAllRecentTvShow().single()
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
            val result = manageRecentTvShowWatchedUseCase.getAllRecentTvShow().single()
            // Then
            assertThat(result).isEmpty()
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
        val result = manageRecentTvShowWatchedUseCase.getAllRecentTvShow(limit = 2).single()
        // Then
        assertThat(result).hasSize(2)
    }
    // endregion

    companion object {
        val tvShow = TvShow(
            id = 1,
            name = "tv1",
            posterPicture = "none",
            releaseYear = 1,
            rating = 1,
            genres = listOf(TvShowGenre.TALK, TvShowGenre.TALK, TvShowGenre.TALK),
        )
        private val tvShowList = listOf(tvShow, tvShow.copy(id = 2), tvShow.copy(id = 3))
    }
}
