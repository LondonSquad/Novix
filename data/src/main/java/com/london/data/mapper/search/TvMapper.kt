@file:KoverIgnore

package com.london.data.mapper.search

import com.london.data.local.model.search.SearchTvShowDtoLocal
import com.london.data.local.model.search.SearchTvShowLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.extractYear
import com.london.data.utils.generateHash
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.TvShow

fun SearchTvShowDtoLocal.toEntity() = TvShow(
    id = id,
    posterPicture = posterPath.asImageUrlOrEmpty(),
    name = name,
    releaseYear = firstAirDate.extractYear(),
    rating = voteAverage.toInt(),
    genres = genreIds,
)

fun ApiResponse<SearchTvShowRemote>.toLocal(query: String) = SearchTvShowLocal(
    date = System.currentTimeMillis(),
    query = query.generateHash(),
    page = currentPage,
    results = items.map { it.toLocal() },
    totalPages = totalPages,
    totalResults = totalItems
)

fun SearchTvShowRemote.toLocal() = SearchTvShowDtoLocal(
    backdropUrl = backdropPath.orEmpty(),
    genreIds = genreIds.orEmpty(),
    id = id.orZero(),
    posterPath = posterPath.orEmpty(),
    name = name.orEmpty(),
    voteAverage = voteAverage.orZero().roundToDecimal(),
    firstAirDate = firstAirDate.orEmpty(),
)

fun SearchTvShowRemote.toEntity(): TvShow = TvShow(
    id = id.orZero(),
    name = name.orEmpty(),
    posterPicture = posterPath.asImageUrlOrEmpty(),
    releaseYear = firstAirDate.orEmpty().extractYear(),
    rating = voteAverage.orZero().toInt(),
    genres = genreIds.orEmpty()
)
