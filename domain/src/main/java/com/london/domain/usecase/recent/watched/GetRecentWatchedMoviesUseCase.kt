package com.london.domain.usecase.recent.watched

import com.london.domain.entity.Movie
import com.london.domain.repository.RecentWatchedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecentWatchedMoviesUseCase @Inject constructor(
    private val recentWatchedRepository: RecentWatchedRepository
) {

    suspend fun getAll(limit: Int? = null, genreId: Int? = null): Flow<List<Movie>> =
        recentWatchedRepository.getAllRecentWatchedMovies().map { shows ->
            shows.filter {
                genreId == null || it.genreIds.contains(genreId)
            }.let { filtered ->
                if (limit != null) filtered.take(limit) else filtered
            }
        }


    suspend fun getMostRecent(): Flow<List<Movie>> = getAll(limit = MOST_RECENT_LIMIT)

    private companion object {
        const val MOST_RECENT_LIMIT = 10
    }
}
