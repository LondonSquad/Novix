package com.london.data.mapper

import com.london.data.datasource.local.model.KnownForDtoLocal
import com.london.data.datasource.local.model.PersonDtoLocal
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.KnownFor
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.util.generateHash
import com.london.domain.KoverIgnore
import com.london.domain.entity.Actor

fun PersonDtoLocal.toActorEntity(): Actor {
    return Actor(
        id = this.id,
        name = this.name,
        profilePicture = "https://image.tmdb.org/t/p/w500${this.profilePath}"
    )
}

fun ApiResponse<SearchActorRemote>.toLocal(query: String): SearchActorsLocal {
    return SearchActorsLocal(
        date = System.currentTimeMillis(),
        query = query.generateHash(),
        page = this.currentPage,
        results = this.items.map { it.toLocal() },
        totalPages = this.totalPages,
        totalResults = this.totalItems
    )
}

@KoverIgnore
fun SearchActorRemote.toLocal(): PersonDtoLocal {
    return PersonDtoLocal(
        adult = this.adult,
        gender = this.gender,
        id = this.id,
        knownForDepartment = this.knownForDepartment ?: "",
        name = this.name ?: "",
        originalName = this.originalName ?: "",
        popularity = this.popularity,
        profilePath = this.profilePath,
        knownFor = this.knownFor.map { it.toKnownForDtoLocal() }
    )
}

@KoverIgnore
fun KnownFor.toKnownForDtoLocal(): KnownForDtoLocal {
    return KnownForDtoLocal(
        adult = this.adult,
        backdropPath = this.backdropPath,
        id = this.id,
        title = this.title,
        originalTitle = this.originalTitle,
        overview = this.overview,
        posterPath = this.posterPath,
        mediaType = this.mediaType ?: "",
        originalLanguage = this.originalLanguage ?: "",
        genreIds = this.genreIds,
        popularity = this.popularity,
        releaseDate = this.releaseDate,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        name = this.name,
        originalName = this.originalName,
        firstAirDate = this.firstAirDate,
        originCountry = this.originCountry
    )
}