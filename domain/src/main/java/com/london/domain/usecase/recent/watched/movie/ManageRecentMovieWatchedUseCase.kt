package com.london.domain.usecase.recent.watched.movie

import com.london.domain.entity.Movie
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.repository.RecentWatchedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ManageRecentMovieWatchedUseCase @Inject constructor(
    private val recentWatchedRepository: RecentWatchedRepository
) {
    suspend fun addMovieToRecentWatched(item: Movie) = recentWatchedRepository.insertMovie(item)

    suspend fun getMostRecent(limit: Int = MOST_RECENT_LIMIT): Flow<List<Movie>> =
        getAllWatchedMovies(limit = limit)

    suspend fun getAllWatchedMovies(
        limit: Int? = null, genre: MovieGenre = MovieGenre.ALL
    ) = recentWatchedRepository.getAllRecentWatchedMovies().map { shows ->
        shows.filter {
            genre == MovieGenre.ALL || it.genres.contains(genre)
        }.let { filtered ->
            if (limit != null) filtered.take(limit) else filtered
        }
    }

    private companion object {
        const val MOST_RECENT_LIMIT = 10
    }
}
