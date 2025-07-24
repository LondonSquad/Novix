package com.london.domain.usecase

import com.london.domain.repository.RecentWatchedRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetRecentWatchedTvShowsUseCase(
    @Provided
    @Named("recentWatchedRepository")
    private val recentWatchedRepository: RecentWatchedRepository
) {
    suspend fun invoke() = recentWatchedRepository.getAllRecentWatchedTvShows()
}