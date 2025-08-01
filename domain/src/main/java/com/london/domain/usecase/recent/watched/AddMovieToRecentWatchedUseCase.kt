package com.london.domain.usecase.recent.watched

import com.london.domain.entity.Movie
import com.london.domain.repository.RecentWatchedRepository
import javax.inject.Inject

class AddMovieToRecentWatchedUseCase @Inject constructor(
    private val recentWatchedRepository: RecentWatchedRepository
) {

    suspend fun invoke(item: Movie) = recentWatchedRepository.insertMovie(item)
}
