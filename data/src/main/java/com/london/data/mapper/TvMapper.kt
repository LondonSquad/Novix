package com.london.data.mapper

import com.london.data.datasource.local.model.SearchTvShowDtoLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.data.datasource.util.generateHash
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.KoverIgnore
import com.london.domain.entity.TvShow

fun SearchTvShowDtoLocal.toTvShowEntity(): TvShow {
    return TvShow(
        id = id,
        posterPicture = posterPath.asImageUrlOrEmpty(),
        name = name,
        releaseYear = if (firstAirDate.isNotEmpty()) firstAirDate.split("-")[0].toInt() else 0,
        rating = voteAverage.toInt(),
        genres = genreIds,
    )
}

fun ApiResponse<SearchTvShowRemote>.toLocal(query: String): SearchTvShowLocal {
    return SearchTvShowLocal(
        date = System.currentTimeMillis(),
        query = query.generateHash(),
        page = currentPage,
        results = items.map { it.toLocalDto() },
        totalPages = totalPages,
        totalResults = totalItems
    )
}

@KoverIgnore
fun SearchTvShowRemote.toLocalDto(): SearchTvShowDtoLocal {
    return SearchTvShowDtoLocal(
        adult = adult,
        backdropUrl = backdropPath.orEmpty(),
        genreIds = genreIds,
        id = id,
        originCountry = originCountry ?: emptyList(),
        originalLanguage = originalLanguage.orEmpty(),
        originalName = originalName.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity,
        posterPath = posterPath.orEmpty(),
        firstAirDate = firstAirDate.orEmpty(),
        name = name.orEmpty(),
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}
