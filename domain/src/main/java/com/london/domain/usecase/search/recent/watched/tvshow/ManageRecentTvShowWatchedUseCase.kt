package com.london.domain.usecase.search.recent.watched.tvshow

import com.london.domain.entity.TvShow
import com.london.domain.repository.RecentWatchedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ManageRecentTvShowWatchedUseCase @Inject constructor(
    private val recentWatchedRepository: RecentWatchedRepository
) {
    suspend fun addTvShowToRecentWatched(item: TvShow) = recentWatchedRepository.insertTvShow(item)

    suspend fun getAllRecentTvShow(
        limit: Int? = null, genreId: Int? = null
    ): Flow<List<TvShow>> =
        recentWatchedRepository.getAllRecentWatchedTvShows().map { shows ->
            shows.filter {
                genreId == null || it.genres.contains(genreId)
            }.let { filtered ->
                if (limit != null) filtered.take(limit) else filtered
            }
        }

    suspend fun getMostRecent(limit: Int = MOST_RECENT_LIMIT): Flow<List<TvShow>> =
        getAllRecentTvShow(limit = limit)

    private companion object {
        const val MOST_RECENT_LIMIT = 10
    }
}
