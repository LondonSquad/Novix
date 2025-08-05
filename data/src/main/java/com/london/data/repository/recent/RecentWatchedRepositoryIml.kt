package com.london.data.repository.recent

import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.local.source.recent.watched.RecentWatchedDataSource
import com.london.data.mapper.recent.toEntity
import com.london.data.mapper.recent.toRecentWatchedMovieLocal
import com.london.data.mapper.recent.toRecentWatchedTvShowLocal
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.repository.RecentWatchedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class RecentWatchedRepositoryIml @Inject constructor(
    private val recentWatchedMoviesDataSource: RecentWatchedDataSource<RecentWatchedMovieLocal>,
    private val recentWatchedTvShowsDataSource: RecentWatchedDataSource<RecentWatchedTvShowLocal>
) : RecentWatchedRepository {
    override suspend fun getAllRecentWatchedMovies(): Flow<List<Movie>> =
        recentWatchedMoviesDataSource.getAll().map { list -> list.map { item -> item.toEntity() } }

    override suspend fun insertMovie(item: Movie) =
        recentWatchedMoviesDataSource.insert(item.toRecentWatchedMovieLocal())

    override suspend fun getAllRecentWatchedTvShows(): Flow<List<TvShow>> =
        recentWatchedTvShowsDataSource.getAll().map { list -> list.map { item -> item.toEntity() } }

    override suspend fun insertTvShow(item: TvShow) =
        recentWatchedTvShowsDataSource.insert(item.toRecentWatchedTvShowLocal())
}
