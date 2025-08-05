package com.london.data.local.source.recent.watched

import com.london.data.local.database.dao.recent.watched.movie.RecentWatchedMoviesDao
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class RecentWatchedMoviesDataSource @Inject constructor(
    private val recentWatchedMoviesDao: RecentWatchedMoviesDao
) : RecentWatchedDataSource<RecentWatchedMovieLocal> {

    override suspend fun getAll(): Flow<List<RecentWatchedMovieLocal>> =
        recentWatchedMoviesDao.getAll().catch { emit(emptyList()) }.distinctUntilChanged()

    override suspend fun insert(item: RecentWatchedMovieLocal) {
        runCatching {
            recentWatchedMoviesDao.insert(item)
        }
    }
}
