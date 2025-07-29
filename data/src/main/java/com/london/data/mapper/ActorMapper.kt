@file:KoverIgnore

package com.london.data.mapper

import com.london.data.local.model.search.ActorLocal
import com.london.data.local.model.search.KnownForLocal
import com.london.data.local.model.search.SearchActorsLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.searchactormodel.KnownFor
import com.london.data.remote.model.search.model.searchactormodel.SearchActorRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.generateHash
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.Actor

fun ActorLocal.toEntity(
    characterName: String,
) = Actor(
    id = id,
    name = name,
    profilePicture = profileUrl.asImageUrlOrEmpty(),
    characterName = characterName
)

fun ApiResponse<SearchActorRemote>.toLocal(query: String) = SearchActorsLocal(
    date = System.currentTimeMillis(),
    query = query.generateHash(),
    page = currentPage,
    results = items.map { it.toLocal() },
    totalPages = totalPages,
    totalResults = totalItems
)

fun SearchActorRemote.toLocal() = ActorLocal(
    adult = adult.isTrue,
    gender = gender.orZero(),
    id = id.orZero(),
    knownForDepartment = knownForDepartment.orEmpty(),
    name = name.orEmpty(),
    originalName = originalName.orEmpty(),
    popularity = popularity.orZero(),
    profileUrl = profilePath.orEmpty(),
    knownFor = knownFor?.map { it.toLocal() }.orEmpty()
)

fun KnownFor.toLocal() = KnownForLocal(
    adult = adult.isTrue,
    backdropPath = backdropPath.orEmpty(),
    id = id.orZero(),
    title = title.orEmpty(),
    originalTitle = originalTitle.orEmpty(),
    overview = overview.orEmpty(),
    posterUrl = posterPath.orEmpty(),
    mediaType = mediaType.orEmpty(),
    originalLanguage = originalLanguage.orEmpty(),
    genreIds = genreIds.orEmpty(),
    popularity = popularity.orZero(),
    releaseDate = releaseDate.orEmpty(),
    video = video.isTrue,
    voteAverage = voteAverage.orZero().roundToDecimal(),
    voteCount = voteCount.orZero(),
    name = name.orEmpty(),
    originalName = originalName.orEmpty(),
    firstAirDate = firstAirDate.orEmpty(),
    originCountry = originCountry.orEmpty()
)