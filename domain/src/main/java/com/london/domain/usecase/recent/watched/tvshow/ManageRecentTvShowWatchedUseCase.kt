package com.london.domain.usecase.recent.watched.tvshow

import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.repository.RecentWatchedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ManageRecentTvShowWatchedUseCase @Inject constructor(
    private val recentWatchedRepository: RecentWatchedRepository
) {
    suspend fun addTvShowToRecentWatched(item: TvShow) = recentWatchedRepository.insertTvShow(item)

    suspend fun getAllRecentTvShow(
        limit: Int? = null, genre: TvShowGenre = TvShowGenre.ALL
    ): Flow<List<TvShow>> =
        recentWatchedRepository.getAllRecentWatchedTvShows().map { shows ->
            shows.filter {
                genre == TvShowGenre.ALL || it.genres.contains(genre)
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
