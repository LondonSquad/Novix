package com.london.domain.usecase.recent.watched

import com.london.domain.repository.RecentWatchedRepository
import javax.inject.Inject

class GetRecentWatchedTvShowsUseCase @Inject constructor(
    private val recentWatchedRepository: RecentWatchedRepository
) {
    suspend fun invoke(limit: Int? = null, genreId: Int? = null) =
        recentWatchedRepository.getAllRecentWatchedTvShows().filter {
            genreId == null || it.genres.contains(genreId)
        }.take(limit ?: Int.MAX_VALUE)
}
