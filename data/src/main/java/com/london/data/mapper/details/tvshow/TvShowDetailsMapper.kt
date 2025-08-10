package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.model.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowGenre
import com.london.data.remote.model.details.tvshow.model.TvShowSeason
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowGenreEntity
import com.london.domain.entity.tvshowdetails.TvShowSeasonEntity

fun TvShowDetailsRemoteResponse.toEntity() = TvShowDetailsEntity(
    firstAirDate = firstAirDate.orEmpty(),
    tvShowGenres = tvShowGenres?.map { it.toEntity() }.orEmpty(),
    id = id.orZero(),
    name = name.orEmpty(),
    numberOfEpisodes = numberOfEpisodes.orZero(),
    numberOfSeasons = numberOfSeasons.orZero(),
    overview = overview.orEmpty(),
    posterUrl = posterPath.asImageUrlOrEmpty(),
    tvShowSeasons = tvShowSeasons?.map { it.toEntity() }.orEmpty(),
    voteAverage = voteAverage.orZero().roundToDecimal(),
)

fun TvShowGenre.toEntity() = TvShowGenreEntity(
    id = id.orZero(),
    name = name.orEmpty()
)

fun TvShowSeason.toEntity() = TvShowSeasonEntity(
    airDate = airDate,
    episodeCount = episodeCount.orZero(),
    id = id.orZero(),
    name = name.orEmpty(),
    overview = overview.orEmpty(),
    posterUrl = posterPath,
    seasonNumber = seasonNumber.orZero(),
    voteAverage = voteAverage.orZero().roundToDecimal(),
)
