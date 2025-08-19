package com.london.data.mapper.recent

import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.mapper.genre.getId
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.tvshow.TvShow


fun RecentWatchedMovieLocal.toEntity() = Movie(
    id = id,
    name = name,
    posterUrl = posterPictureUrl,
    releaseYear = releaseYear,
    rating = rating,
    genres = genreIds.toMovieGenre()
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
    genres = genres.toTvShowGenre()
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
