package com.london.data.mapper

import com.london.data.datasource.local.model.KnownForDtoLocal
import com.london.data.datasource.local.model.PersonDtoLocal
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.remote.search.KnownForDto
import com.london.data.datasource.remote.search.PersonDto
import com.london.data.datasource.remote.search.SearchActorsResponse
import com.london.data.datasource.util.generateHash
import com.london.domain.entity.Actor

fun PersonDtoLocal.toActorEntity(): Actor {
    return Actor(
        id = this.id,
        name = this.name,
        profilePicture = this.profilePath ?: ""
    )
}
fun SearchActorsResponse.toLocal(query: String): SearchActorsLocal{
    return SearchActorsLocal(
        date = this.page.toLong(),
        query = query.generateHash(),
        page = this.page,
        results = this.results.map { it.toLocal() },
        totalPages = this.totalPages,
        totalResults = this.totalResults
    )
}

fun PersonDto.toLocal(): PersonDtoLocal {
    return PersonDtoLocal(
        adult = this.adult,
        gender = this.gender,
        id = this.id,
        knownForDepartment = this.knownForDepartment,
        name = this.name,
        originalName = this.originalName,
        popularity = this.popularity,
        profilePath = this.profilePath,
        knownFor = this.knownFor.map { it.toKnownForDtoLocal() }
    )
}

fun KnownForDto.toKnownForDtoLocal(): KnownForDtoLocal {
    return KnownForDtoLocal(
        adult = this.adult,
        backdropPath = this.backdropPath,
        id = this.id,
        title = this.title,
        originalTitle = this.originalTitle,
        overview = this.overview,
        posterPath = this.posterPath,
        mediaType = this.mediaType,
        originalLanguage = this.originalLanguage,
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