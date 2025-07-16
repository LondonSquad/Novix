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
    adult = this.adult,
    backdropPath = "https://image.tmdb.org/t/p/w500${this.backdropPath}",
    createdBy = this.createdBy.map { it.toEntity() },
    episodeRunTime = this.episodeRunTime,
    firstAirDate = this.firstAirDate,
    tvShowGenres = this.tvShowGenres.map { it.toEntity() },
    homepage = this.homepage,
    id = this.id,
    inProduction = this.inProduction,
    languages = this.languages,
    lastAirDate = this.lastAirDate,
    lastTvShowEpisodeToAir = this.lastTvShowEpisodeToAir?.toEntity(),
    name = this.name,
    nextTvShowEpisodeToAir = this.nextTvShowEpisodeToAir?.toEntity(),
    tvShowNetworks = this.tvShowNetworks.map { it.toEntity() },
    numberOfEpisodes = this.numberOfEpisodes,
    numberOfSeasons = this.numberOfSeasons,
    originCountry = this.originCountry,
    originalLanguage = this.originalLanguage,
    originalName = this.originalName,
    overview = this.overview,
    popularity = this.popularity,
    posterPath = "https://image.tmdb.org/t/p/w500${this.posterPath}",
    productionCompanies = this.productionCompanies.map { it.toEntity() },
    productionCountries = this.productionCountries.map { it.toEntity() },
    tvShowSeasons = this.tvShowSeasons.map { it.toEntity() },
    tvShowSpokenLanguageEntities = this.tvShowSpokenLanguages.map { it.toEntity() },
    status = this.status,
    tagline = this.tagline,
    type = this.type,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount
)

fun TvShowCreator.toEntity() = TvShowCreatorEntity(
    id = this.id,
    creditId = this.creditId,
    name = this.name,
    originalName = this.originalName,
    gender = this.gender,
    profilePath = "https://image.tmdb.org/t/p/w500${this.profilePath}"
)

fun TvShowGenre.toEntity() = TvShowGenreEntity(
    id = this.id,
    name = this.name
)

fun TvShowEpisode.toEntity() = TvShowEpisodeEntity(
    id = this.id,
    name = this.name,
    overview = this.overview,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount,
    airDate = this.airDate,
    episodeNumber = this.episodeNumber,
    episodeType = this.episodeType,
    productionCode = this.productionCode,
    runtime = this.runtime,
    seasonNumber = this.seasonNumber,
    showId = this.showId,
    stillPath = this.stillPath
)

fun TvShowNetwork.toEntity() = TvShowNetworkEntity(
    id = this.id,
    logoPath = "https://image.tmdb.org/t/p/w500${this.logoPath}",
    name = this.name,
    originCountry = this.originCountry
)

fun TvShowProductionCompany.toEntity() = TvShowProductionCompanyEntity(
    id = this.id,
    logoPath = "https://image.tmdb.org/t/p/w500${this.logoPath}",
    name = this.name,
    originCountry = this.originCountry
)

@KoverIgnore
fun TvShowProductionCountry.toEntity() = TvShowProductionCountryEntity(
    iso31661 = this.iso31661,
    name = this.name
)

@KoverIgnore
fun TvShowSeason.toEntity() = TvShowSeasonEntity(
    airDate = this.airDate,
    episodeCount = this.episodeCount,
    id = this.id,
    name = this.name,
    overview = this.overview,
    posterPath = this.posterPath,
    seasonNumber = this.seasonNumber,
    voteAverage = this.voteAverage
)

@KoverIgnore
fun TvShowSpokenLanguage.toEntity() = TvShowSpokenLanguageEntity(
    englishName = this.englishName,
    iso6391 = this.iso6391,
    name = this.name
)
