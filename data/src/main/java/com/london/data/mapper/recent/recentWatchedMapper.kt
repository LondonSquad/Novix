@file:KoverIgnore

package com.london.data.mapper.recent

import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.mapper.genre.getId
import com.london.data.mapper.genre.toMovieGenre
import com.london.data.mapper.genre.toTvShowGenre
import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow


fun RecentWatchedMovieLocal.toEntity() = Movie(
    id = id,
    name = name,
    posterUrl = posterPictureUrl,
    releaseYear = releaseYear,
    rating = rating,
    genres = genreIds.map { it.toMovieGenre() }
)

fun Movie.toRecentWatchedMovieLocal() = RecentWatchedMovieLocal(
    id = id,
    name = name,
    posterPictureUrl = posterUrl,
    releaseYear = releaseYear,
    rating = rating,
    genreIds = genres.map { it.getId() },
    watchedAt = System.currentTimeMillis()
)

fun RecentWatchedTvShowLocal.toEntity() = TvShow(
    id = id,
    name = name,
    posterPicture = posterPictureUrl,
    releaseYear = releaseYear,
    rating = rating,
    genres = genres.map { it.toTvShowGenre() }
)

fun TvShow.toRecentWatchedTvShowLocal() = RecentWatchedTvShowLocal(
    id = id,
    name = name,
    posterPictureUrl = posterPicture,
    releaseYear = releaseYear,
    rating = rating,
    genres = genres.map { it.getId() },
    watchedAt = System.currentTimeMillis()
)
