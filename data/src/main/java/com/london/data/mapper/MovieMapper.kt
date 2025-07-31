@file:KoverIgnore

package com.london.data.mapper

import com.london.data.local.model.search.SearchMovieDtoLocal
import com.london.data.local.model.search.SearchMoviesLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.SearchMovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.extractYear
import com.london.data.utils.generateHash
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.data.utils.roundToFirstDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie

fun SearchMovieDtoLocal.toEntity() = Movie(
    id = id,
    posterUrl = posterPath.asImageUrlOrEmpty(),
    genreIds = genreIds,
    name = name,
    releaseYear =releaseYear,
    rating = rating,
)

fun ApiResponse<SearchMovieRemote>.toLocal(query: String) = SearchMoviesLocal(
    date = System.currentTimeMillis(),
    query = query.generateHash(),
    page = currentPage,
    results = items.map { it.toLocal() },
    totalPages = totalPages,
    totalResults = totalItems
)

fun SearchMovieRemote.toLocal() = SearchMovieDtoLocal(
    genreIds = genreIds.orEmpty(),
    id = id.orZero(),
    posterPath = posterPath.orEmpty(),
    name = name.orEmpty(),
    releaseYear = releaseDate?.extractYear().orZero(),
    rating = voteAverage?.toInt().orZero(),
)

fun SearchMovieRemote.toEntity() = Movie(
    id = id.orZero(),
    name = name.orEmpty(),
    posterUrl = posterPath.asImageUrlOrEmpty(),
    releaseYear = releaseDate?.extractYear().orZero(),
    rating = voteAverage?.toInt().orZero(),
    genreIds = genreIds.orEmpty()
)