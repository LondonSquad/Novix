package com.london.data.mapper

import com.london.data.local.model.search.SearchMovieDtoLocal
import com.london.data.local.model.search.SearchMoviesLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.SearchMovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.generateHash
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie

fun SearchMovieDtoLocal.toMovieEntity(): Movie {
    return Movie(
        id = id,
        posterPicture = posterPath.asImageUrlOrEmpty(),
        name = title,
        releaseYear = if (releaseDate.isNotEmpty())
            releaseDate.split("-")[0].toInt() else 0,
        rating = voteAverage.toInt(),
        genreIds = genreIds,
    )
}

fun ApiResponse<SearchMovieRemote>.toLocal(query: String): SearchMoviesLocal {
    return SearchMoviesLocal(
        date = System.currentTimeMillis(),
        query = query.generateHash(),
        page = currentPage,
        results = items.map { it.toMovieLocalDto() },
        totalPages = totalPages,
        totalResults = totalItems
    )
}

@KoverIgnore
fun SearchMovieRemote.toMovieLocalDto(): SearchMovieDtoLocal {
    return SearchMovieDtoLocal(
        adult = adult.isTrue,
        backdropPath = backdropPath,
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterPath = posterPath,
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        video = video.isTrue,
        voteAverage = voteAverage.orZero().roundToDecimal(),
        voteCount = voteCount.orZero()
    )
}