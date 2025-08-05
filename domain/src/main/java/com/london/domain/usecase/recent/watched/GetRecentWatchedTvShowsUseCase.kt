package com.london.domain.usecase.recent.watched

import com.london.domain.entity.TvShow
import com.london.domain.repository.RecentWatchedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecentWatchedTvShowsUseCase @Inject constructor(
    private val recentWatchedRepository: RecentWatchedRepository
) {
    suspend fun getAll(limit: Int? = null, genreId: Int? = null): Flow<List<TvShow>> =
        recentWatchedRepository.getAllRecentWatchedTvShows().map { shows ->
            shows.filter {
                genreId == null || it.genres.contains(genreId)
            }.let { filtered ->
                if (limit != null) filtered.take(limit) else filtered
            }
        }

    suspend fun getMostRecent(): Flow<List<TvShow>> = getAll(limit = MOST_RECENT_LIMIT)

    private companion object {
        const val MOST_RECENT_LIMIT = 10
    }
}