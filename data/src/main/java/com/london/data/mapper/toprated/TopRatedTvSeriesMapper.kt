package com.london.data.mapper.toprated

import com.london.data.remote.model.toprated.tvshow.model.TopRatedTvSeriesRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.entity.toprated.TopRatedTvSeries

fun TopRatedTvSeriesRemote.toEntity(): TopRatedTvSeries {
    return TopRatedTvSeries(
        adult = adult.isTrue,
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        firstAirDate = firstAirDate.orEmpty(),
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        name = name.orEmpty(),
        originCountry = originCountry.orEmpty(),
        originalLanguage = originalLanguage.orEmpty(),
        originalName = originalName.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero()
    )
}
