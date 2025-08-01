package com.london.data.local.source.recent.watched

import com.london.data.local.database.dao.recent.whatched.movie.RecentWatchedMoviesDao
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import javax.inject.Inject

class RecentWatchedMoviesDataSource @Inject constructor(
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
