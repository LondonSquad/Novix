package com.london.domain.usecase

import com.london.domain.repository.RecentWatchedRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetRecentWatchedMoviesUseCase(
    @Provided
    @Named("recentWatchedRepository")
    private val recentWatchedRepository: RecentWatchedRepository
) {

    suspend fun invoke(limit: Int? = null) =
        recentWatchedRepository.getAllRecentWatchedMovies().take(limit ?: Int.MAX_VALUE)
}
