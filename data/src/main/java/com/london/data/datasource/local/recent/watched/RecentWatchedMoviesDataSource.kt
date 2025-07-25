package com.london.data.datasource.local.recent.watched

import com.london.data.datasource.local.dao.recent.whatched.movie.RecentWatchedMoviesDao
import com.london.data.datasource.local.model.recent.watched.RecentWatchedMovieLocal
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
@Named("recentWatchedMoviesDataSource")
class RecentWatchedMoviesDataSource(
    @Provided
    @Named("recentWatchedMoviesDao")
    private val recentWatchedMoviesDao:  RecentWatchedMoviesDao
) : RecentWatchedDataSource<RecentWatchedMovieLocal> {

    override suspend fun getAll(): List<RecentWatchedMovieLocal> = runCatching {
        recentWatchedMoviesDao.getAll()
    }.getOrDefault(emptyList())

    override suspend fun insert(item: RecentWatchedMovieLocal) {
        runCatching {
            recentWatchedMoviesDao.insert(item)
        }
    }
}
