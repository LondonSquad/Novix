package com.london.domain.usecase.recent.watched

import com.london.domain.entity.TvShow
import com.london.domain.repository.RecentWatchedRepository
import javax.inject.Inject

class AddTvShowToRecentWatchedUseCase @Inject constructor(
    private val recentWatchedRepository: RecentWatchedRepository
) {

    suspend fun invoke(item: TvShow) = recentWatchedRepository.insertTvShow(item)
}
