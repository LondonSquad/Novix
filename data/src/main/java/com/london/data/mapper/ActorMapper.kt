@file:KoverIgnore
package com.london.data.mapper
import com.london.data.local.model.search.KnownForDtoLocal
import com.london.data.local.model.search.PersonDtoLocal
import com.london.data.local.model.search.SearchActorsLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.KnownFor
import com.london.data.remote.model.search.model.SearchActorRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.generateHash
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.Actor

fun PersonDtoLocal.toActorEntity(): Actor {
    return Actor(
        id = id,
        name = name,
        profilePicture = profileUrl.asImageUrlOrEmpty()
    )
}

fun ApiResponse<SearchActorRemote>.toLocal(query: String): SearchActorsLocal {
    return SearchActorsLocal(
        date = System.currentTimeMillis(),
        query = query.generateHash(),
        page = currentPage,
        results = items.map { it.toLocal() },
        totalPages = totalPages,
        totalResults = totalItems
    )
}

fun SearchActorRemote.toLocal(): PersonDtoLocal {
    return PersonDtoLocal(
        adult = adult.isTrue,
        gender = gender.orZero(),
        id = id.orZero(),
        knownForDepartment = knownForDepartment.orEmpty(),
        name = name.orEmpty(),
        originalName = originalName.orEmpty(),
        popularity = popularity.orZero(),
        profileUrl = profilePath,
        knownFor = knownFor?.map { it.toKnownForDtoLocal() }.orEmpty()
    )
}

fun KnownFor.toKnownForDtoLocal(): KnownForDtoLocal {
    return KnownForDtoLocal(
        adult = adult.isTrue,
        backdropPath = backdropPath,
        id = id.orZero(),
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        posterUrl = posterPath.asImageUrlOrEmpty(),
        mediaType = mediaType.orEmpty(),
        originalLanguage = originalLanguage.orEmpty(),
        genreIds = genreIds.orEmpty(),
        popularity = popularity.orZero(),
        releaseDate = releaseDate,
        video = video,
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero(),
        name = name,
        originalName = originalName,
        firstAirDate = firstAirDate,
        originCountry = originCountry
    )
}