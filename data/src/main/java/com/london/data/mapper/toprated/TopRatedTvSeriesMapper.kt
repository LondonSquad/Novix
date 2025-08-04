package com.london.data.mapper.toprated

import com.london.data.local.model.home.TopRatedLocal
import com.london.data.remote.model.toprated.TopRatedTvSeriesRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedTvSeries

fun TopRatedTvSeriesRemote.toEntity(): TopRatedTvSeries {
    return TopRatedTvSeries(
        id = id.orZero(),
        name = name.orEmpty(),
        firstAirDate = firstAirDate.orEmpty(),
        genreIds = genreIds.orEmpty(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage.orZero(),
    )
}

fun TopRatedLocal.toTvShow(): TopRatedTvSeries {
    return TopRatedTvSeries(
        id = id,
        name = name,
        firstAirDate = releaseYear,
        genreIds = genre,
        posterUrl = posterPictureUrl,
        voteAverage = rating
    )
}

fun TopRatedTvSeries.toLocal(): TopRatedLocal {
    return TopRatedLocal(
        id = id,
        name = name,
        posterPictureUrl = posterUrl,
        rating = voteAverage,
        releaseYear = firstAirDate,
        mediaType = MediaType.TvShow,
        genre = genreIds
    )
}