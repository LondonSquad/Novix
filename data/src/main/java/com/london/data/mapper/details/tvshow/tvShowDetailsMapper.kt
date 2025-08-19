package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.TvShowDetailsRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.entity.tvshow.TvShowDetails

fun TvShowDetailsRemoteResponse.toEntity() = TvShowDetails(
    firstAirDate = firstAirDate.orEmpty(),
    tvShowGenres = tvShowGenres?.map { it.id.orZero() }.toTvShowGenre(),
    id = id.orZero(),
    name = name.orEmpty(),
    numberOfEpisodes = numberOfEpisodes.orZero(),
    numberOfSeasons = numberOfSeasons.orZero(),
    overview = overview.orEmpty(),
    posterUrl = posterPath.asImageUrlOrEmpty(),
    tvShowSeasons = tvShowSeasons.orEmpty().map { it.seasonNumber.orZero() },
    voteAverage = voteAverage.orZero().roundToDecimal(),
)

