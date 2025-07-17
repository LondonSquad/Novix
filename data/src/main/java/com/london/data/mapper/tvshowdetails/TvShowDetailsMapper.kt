package com.london.data.mapper.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCreator
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowEpisode
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowGenre
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowNetwork
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowProductionCompany
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowProductionCountry
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowSeason
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowSpokenLanguage
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshowdetails.TvShowCreatorEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowEpisodeEntity
import com.london.domain.entity.tvshowdetails.TvShowGenreEntity
import com.london.domain.entity.tvshowdetails.TvShowNetworkEntity
import com.london.domain.entity.tvshowdetails.TvShowProductionCompanyEntity
import com.london.domain.entity.tvshowdetails.TvShowProductionCountryEntity
import com.london.domain.entity.tvshowdetails.TvShowSeasonEntity
import com.london.domain.entity.tvshowdetails.TvShowSpokenLanguageEntity

fun TvShowDetailsRemoteResponse.toEntity() = TvShowDetailsEntity(
    adult = adult,
    backdropPath = backdropPath.asImageUrlOrEmpty(),
    createdBy = createdBy.map { it.toEntity() },
    episodeRunTime = episodeRunTime,
    firstAirDate = firstAirDate,
    tvShowGenres = tvShowGenres.map { it.toEntity() },
    homepage = homepage,
    id = id,
    inProduction = inProduction,
    languages = languages,
    lastAirDate = lastAirDate,
    lastTvShowEpisodeToAir = lastTvShowEpisodeToAir?.toEntity(),
    name = name,
    nextTvShowEpisodeToAir = nextTvShowEpisodeToAir?.toEntity(),
    tvShowNetworks = tvShowNetworks.map { it.toEntity() },
    numberOfEpisodes = numberOfEpisodes,
    numberOfSeasons = numberOfSeasons,
    originCountry = originCountry,
    originalLanguage = originalLanguage,
    originalName = originalName,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath.asImageUrlOrEmpty(),
    productionCompanies = productionCompanies.map { it.toEntity() },
    productionCountries = productionCountries.map { it.toEntity() },
    tvShowSeasons = tvShowSeasons.map { it.toEntity() },
    tvShowSpokenLanguageEntities = tvShowSpokenLanguages.map { it.toEntity() },
    status = status,
    tagline = tagline,
    type = type,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun TvShowCreator.toEntity() = TvShowCreatorEntity(
    id = id,
    creditId = creditId,
    name = name,
    originalName = originalName,
    gender = gender,
    profilePath = profilePath.asImageUrlOrEmpty()
)

fun TvShowGenre.toEntity() = TvShowGenreEntity(
    id = id,
    name = name
)

fun TvShowEpisode.toEntity() = TvShowEpisodeEntity(
    id = id,
    name = name,
    overview = overview,
    voteAverage = voteAverage,
    voteCount = voteCount,
    airDate = airDate,
    episodeNumber = episodeNumber,
    episodeType = episodeType,
    productionCode = productionCode,
    runtime = runtime,
    seasonNumber = seasonNumber,
    showId = showId,
    stillPath = stillPath
)

fun TvShowNetwork.toEntity() = TvShowNetworkEntity(
    id = id,
    logoPath = logoPath.asImageUrlOrEmpty(),
    name = name,
    originCountry = originCountry
)

fun TvShowProductionCompany.toEntity() = TvShowProductionCompanyEntity(
    id = id,
    logoPath = logoPath.asImageUrlOrEmpty(),
    name = name,
    originCountry = originCountry
)

@KoverIgnore
fun TvShowProductionCountry.toEntity() = TvShowProductionCountryEntity(
    iso31661 = iso31661,
    name = name
)

@KoverIgnore
fun TvShowSeason.toEntity() = TvShowSeasonEntity(
    airDate = airDate,
    episodeCount = episodeCount,
    id = id,
    name = name,
    overview = overview,
    posterPath = posterPath,
    seasonNumber = seasonNumber,
    voteAverage = voteAverage
)

@KoverIgnore
fun TvShowSpokenLanguage.toEntity() = TvShowSpokenLanguageEntity(
    englishName = englishName,
    iso6391 = iso6391,
    name = name
)
