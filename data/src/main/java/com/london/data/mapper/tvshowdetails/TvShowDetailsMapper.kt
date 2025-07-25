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
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
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
    adult = adult.isTrue,
    backdropUrl = backdropPath.asImageUrlOrEmpty(),
    createdBy = createdBy?.map { it.toEntity() }.orEmpty(),
    episodeRunTime = episodeRunTime.orEmpty(),
    firstAirDate = firstAirDate.orEmpty(),
    tvShowGenres = tvShowGenres?.map { it.toEntity() }.orEmpty(),
    homepage = homepage.orEmpty(),
    id = id.orZero(),
    inProduction = inProduction.isTrue,
    languages = languages.orEmpty(),
    lastAirDate = lastAirDate.orEmpty(),
    lastTvShowEpisodeToAir = lastTvShowEpisodeToAir?.toEntity(),
    name = name.orEmpty(),
    nextTvShowEpisodeToAir = nextTvShowEpisodeToAir?.toEntity(),
    tvShowNetworks = tvShowNetworks?.map { it.toEntity() }.orEmpty(),
    numberOfEpisodes = numberOfEpisodes.orZero(),
    numberOfSeasons = numberOfSeasons.orZero(),
    originCountry = originCountry.orEmpty(),
    originalLanguage = originalLanguage.orEmpty(),
    originalName = originalName.orEmpty(),
    overview = overview.orEmpty(),
    popularity = popularity.orZero(),
    posterUrl = posterPath.asImageUrlOrEmpty(),
    productionCompanies = productionCompanies?.map { it.toEntity() }.orEmpty(),
    productionCountries = productionCountries?.map { it.toEntity() }.orEmpty(),
    tvShowSeasons = tvShowSeasons?.map { it.toEntity() }.orEmpty(),
    tvShowSpokenLanguageEntities = tvShowSpokenLanguages?.map { it.toEntity() }.orEmpty(),
    status = status.orEmpty(),
    tagline = tagline.orEmpty(),
    type = type.orEmpty(),
    voteAverage = voteAverage.roundToDecimal().orZero(),
    voteCount = voteCount.orZero()
)

fun TvShowCreator.toEntity() = TvShowCreatorEntity(
    id = id.orZero(),
    creditId = creditId.orEmpty(),
    name = name.orEmpty(),
    originalName = originalName.orEmpty(),
    gender = gender.orZero(),
    profileUrl = profilePath.asImageUrlOrEmpty()
)

fun TvShowGenre.toEntity() = TvShowGenreEntity(
    id = id.orZero(),
    name = name.orEmpty()
)

fun TvShowEpisode.toEntity() = TvShowEpisodeEntity(
    id = id.orZero(),
    name = name.orEmpty(),
    overview = overview.orEmpty(),
    voteAverage = voteAverage.orZero(),
    voteCount = voteCount.orZero(),
    airDate = airDate.orEmpty(),
    episodeNumber = episodeNumber.orZero(),
    episodeType = episodeType.orEmpty(),
    productionCode = productionCode.orEmpty(),
    runtime = runtime.orZero(),
    seasonNumber = seasonNumber.orZero(),
    showId = showId.orZero(),
    stillPath = stillPath
)

fun TvShowNetwork.toEntity() = TvShowNetworkEntity(
    id = id.orZero(),
    logoUrl = logoPath.asImageUrlOrEmpty(),
    name = name.orEmpty(),
    originCountry = originCountry.orEmpty()
)

fun TvShowProductionCompany.toEntity() = TvShowProductionCompanyEntity(
    id = id.orZero(),
    logoUrl = logoPath.asImageUrlOrEmpty(),
    name = name.orEmpty(),
    originCountry = originCountry.orEmpty()
)

@KoverIgnore
fun TvShowProductionCountry.toEntity() = TvShowProductionCountryEntity(
    iso31661 = iso31661.orEmpty(),
    name = name.orEmpty()
)

@KoverIgnore
fun TvShowSeason.toEntity() = TvShowSeasonEntity(
    airDate = airDate,
    episodeCount = episodeCount.orZero(),
    id = id.orZero(),
    name = name.orEmpty(),
    overview = overview.orEmpty(),
    posterUrl = posterPath,
    seasonNumber = seasonNumber.orZero(),
    voteAverage = voteAverage.orZero()
)

@KoverIgnore
fun TvShowSpokenLanguage.toEntity() = TvShowSpokenLanguageEntity(
    englishName = englishName.orEmpty(),
    iso6391 = iso6391.orEmpty(),
    name = name.orEmpty()
)
