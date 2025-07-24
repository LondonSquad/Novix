package com.london.data.repository.recent

import com.london.data.datasource.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.datasource.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.datasource.local.recent.watched.RecentWatchedDataSource
import com.london.data.mapper.recent.toEntity
import com.london.data.mapper.recent.toRecentWatchedMovieLocal
import com.london.data.mapper.recent.toRecentWatchedTvShowLocal
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.repository.RecentWatchedRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
@Named("recentWatchedRepository")
class RecentWatchedRepositoryIml(
    @Provided
    @Named("recentWatchedMoviesDataSource")
    private val recentWatchedMoviesDataSource: RecentWatchedDataSource<RecentWatchedMovieLocal>,
    @Provided
    @Named("recentWatchedTvShowsDataSource")
    private val recentWatchedTvShowsDataSource: RecentWatchedDataSource<RecentWatchedTvShowLocal>
) : RecentWatchedRepository {
    override suspend fun getAllRecentWatchedMovies(): List<Movie> =
        recentWatchedMoviesDataSource.getAll().map { it.toEntity() }

    override suspend fun insertMovie(item: Movie) =
        recentWatchedMoviesDataSource.insert(item.toRecentWatchedMovieLocal())

    override suspend fun getAllRecentWatchedTvShows(): List<TvShow> =
        recentWatchedTvShowsDataSource.getAll().map { it.toEntity() }

    override suspend fun insertTvShow(item: TvShow) =
        recentWatchedTvShowsDataSource.insert(item.toRecentWatchedTvShowLocal())
}
