package com.london.domain.usecase

import com.london.domain.entity.Movie
import com.london.domain.repository.RecentWatchedRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class AddMovieToRecentWatchedUseCase(
    @Provided
    @Named("recentWatchedRepository")
    private val recentWatchedRepository: RecentWatchedRepository
) {
    suspend fun invoke(item: Movie) = recentWatchedRepository.insertMovie(item)
}
