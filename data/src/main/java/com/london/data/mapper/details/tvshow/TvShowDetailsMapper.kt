package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.TvShowGenre
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowGenreEntity

fun TvShowDetailsRemoteResponse.toEntity() = TvShowDetailsEntity(
    firstAirDate = firstAirDate.orEmpty(),
    tvShowGenres = tvShowGenres?.map { it.toEntity() }.orEmpty(),
    id = id.orZero(),
    name = name.orEmpty(),
    numberOfEpisodes = numberOfEpisodes.orZero(),
    numberOfSeasons = numberOfSeasons.orZero(),
    overview = overview.orEmpty(),
    posterUrl = posterPath.asImageUrlOrEmpty(),
    tvShowSeasons = tvShowSeasons.orEmpty().map { it.seasonNumber.orZero() },
    voteAverage = voteAverage.orZero().roundToDecimal(),
)

fun TvShowGenre.toEntity() = TvShowGenreEntity(
    id = id.orZero(),
    name = name.orEmpty()
)
