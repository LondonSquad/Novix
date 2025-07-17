package com.london.data.mapper

import com.london.data.datasource.local.model.KnownForDtoLocal
import com.london.data.datasource.local.model.PersonDtoLocal
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.KnownFor
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.util.generateHash
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.KoverIgnore
import com.london.domain.entity.Actor

fun PersonDtoLocal.toActorEntity(): Actor {
    return Actor(
        id = id,
        name = name,
        profilePicture = profilePath.asImageUrlOrEmpty()
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

@KoverIgnore
fun SearchActorRemote.toLocal(): PersonDtoLocal {
    return PersonDtoLocal(
        adult = adult,
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment.orEmpty(),
        name = name.orEmpty(),
        originalName = originalName.orEmpty(),
        popularity = popularity,
        profilePath = profilePath,
        knownFor = knownFor.map { it.toKnownForDtoLocal() }
    )
}

@KoverIgnore
fun KnownFor.toKnownForDtoLocal(): KnownForDtoLocal {
    return KnownForDtoLocal(
        adult = adult,
        backdropPath = backdropPath,
        id = id,
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        mediaType = mediaType.orEmpty(),
        originalLanguage = originalLanguage.orEmpty(),
        genreIds = genreIds,
        popularity = popularity,
        releaseDate = releaseDate,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        name = name,
        originalName = originalName,
        firstAirDate = firstAirDate,
        originCountry = originCountry
    )
}