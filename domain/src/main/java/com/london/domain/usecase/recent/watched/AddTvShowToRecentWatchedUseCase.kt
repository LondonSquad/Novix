package com.london.domain.usecase.recent.watched

import com.london.domain.entity.TvShow
import com.london.domain.repository.RecentWatchedRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class AddTvShowToRecentWatchedUseCase(
    @Provided
    @Named("recentWatchedRepository")
    private val recentWatchedRepository: RecentWatchedRepository
) {
    suspend fun invoke(item: TvShow) = recentWatchedRepository.insertTvShow(item)
}
