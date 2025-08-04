package com.london.data.mapper.home.toprated

import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.remote.model.home.toprated.TopRatedMovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMovie

fun TopRatedMovieRemote.toEntity(): TopRatedMovie {
    return TopRatedMovie(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        voteAverage = voteAverage.orZero(),
        genreIds = genreIds.orEmpty()
    )
}
fun TopRatedLocal.toMovieEntity() : TopRatedMovie {
    return TopRatedMovie(
        id = id.orZero(),
        posterUrl = posterPictureUrl.asImageUrlOrEmpty(),
        releaseDate = releaseYear,
        title = name,
        voteAverage = rating.orZero(),
        genreIds = genre
    )
}

fun TopRatedMovie.toLocal(): TopRatedLocal {
    return TopRatedLocal(
        id = id.orZero(),
        name = title,
        posterPictureUrl = posterUrl.asImageUrlOrEmpty(),
        rating = voteAverage.orZero(),
        releaseYear = releaseDate,
        mediaType = MediaType.Movie,
        genre = genreIds
    )
}