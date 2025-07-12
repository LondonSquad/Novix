package com.london.data.mapper

import com.london.data.datasource.local.model.SearchTvShowDtoLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.remote.search.model.ApiSearch
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.data.datasource.util.generateHash
import com.london.domain.entity.TvShow

fun SearchTvShowDtoLocal.toTvShowEntity(): TvShow {
    return TvShow(
        id = this.id,
        posterPicture = "https://image.tmdb.org/t/p/w500${this.posterPath}",
        name = this.name
    )
}

fun ApiSearch<SearchTvShowRemote>.toLocal(query: String): SearchTvShowLocal {
    return SearchTvShowLocal(
        date = System.currentTimeMillis(),
        query = query.generateHash(),
        page = this.page,
        results = this.results.map { it.toLocalDto() },
        totalPages = this.totalPages,
        totalResults = this.totalResults
    )
}

fun SearchTvShowRemote.toLocalDto(): SearchTvShowDtoLocal {
    return SearchTvShowDtoLocal(
        adult = this.adult,
        backdropPath = this.backdropPath ?: "",
        genreIds = this.genreIds,
        id = this.id,
        originCountry = this.originCountry ?: emptyList(),
        originalLanguage = this.originalLanguage ?: "",
        originalName = this.originalName ?: "",
        overview = this.overview ?: "",
        popularity = this.popularity,
        posterPath = this.posterPath ?: "",
        firstAirDate = this.firstAirDate ?: "",
        name = this.name ?: "",
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}
