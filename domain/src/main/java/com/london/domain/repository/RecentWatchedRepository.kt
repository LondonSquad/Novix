@file:KoverIgnore
package com.london.domain.repository

import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import kotlinx.coroutines.flow.Flow

interface RecentWatchedRepository {
    suspend fun getAllRecentWatchedMovies(): Flow<List<Movie>>
    suspend fun insertMovie(item: Movie)
    suspend fun getAllRecentWatchedTvShows(): Flow<List<TvShow>>
    suspend fun insertTvShow(item: TvShow)
}
