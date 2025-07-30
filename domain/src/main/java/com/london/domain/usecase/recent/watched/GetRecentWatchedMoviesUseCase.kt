package com.london.domain.usecase.recent.watched

import com.london.domain.entity.Movie
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
    suspend fun invoke(limit: Int? = null, genreId: Int? = null): List<Movie> =
        recentWatchedRepository.getAllRecentWatchedMovies().filter {
            genreId == null || it.genreIds.contains(genreId)
        }.take(limit ?: Int.MAX_VALUE)
}
