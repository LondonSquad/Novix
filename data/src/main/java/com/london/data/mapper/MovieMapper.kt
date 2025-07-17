package com.london.data.mapper

import com.london.data.datasource.local.model.SearchMovieDtoLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.util.generateHash
import com.london.data.utils.asImageUrlOrEmpty
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
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}