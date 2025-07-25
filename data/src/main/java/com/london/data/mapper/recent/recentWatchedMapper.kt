@file:KoverIgnore
package com.london.data.mapper.recent

import com.london.data.datasource.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.datasource.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow


fun RecentWatchedMovieLocal.toEntity() = Movie(
    id = id,
    name = name,
    posterPicture = posterPictureUrl,
    releaseYear = releaseYear,
    rating = rating,
    genreIds = genreIds
)

fun Movie.toRecentWatchedMovieLocal() = RecentWatchedMovieLocal(
    id = id,
    name = name,
    posterPictureUrl = posterPicture,
    releaseYear = releaseYear,
    rating = rating,
    genreIds = genreIds,
    watchedAt = System.currentTimeMillis()
)

fun RecentWatchedTvShowLocal.toEntity() = TvShow(
    id = id,
    name = name,
    posterPicture = posterPictureUrl,
    releaseYear = releaseYear,
    rating = rating,
    genres = genres
)

fun TvShow.toRecentWatchedTvShowLocal() = RecentWatchedTvShowLocal(
    id = id,
    name = name,
    posterPictureUrl = posterPicture,
    releaseYear = releaseYear,
    rating = rating,
    genres = genres,
    watchedAt = System.currentTimeMillis()
)

