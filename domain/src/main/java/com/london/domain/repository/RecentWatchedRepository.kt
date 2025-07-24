@file:KoverIgnore
package com.london.domain.repository

import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow

interface RecentWatchedRepository {
    suspend fun getAllRecentWatchedMovies(): List<Movie>
    suspend fun insertMovie(item: Movie)
    suspend fun getAllRecentWatchedTvShows(): List<TvShow>
    suspend fun insertTvShow(item: TvShow)
}
