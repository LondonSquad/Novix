package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow


interface SearchRepository {
    suspend fun searchForMovies(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<Movie>

    suspend fun searchForTvShows(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<TvShow>

    suspend fun searchForActors(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<Actor>

    suspend fun searchForMoviesByCategory(
        categoryId: Int,
        pageNumber: Int
    ): PagedFetchResponse<Movie>

    suspend fun searchForTvShowByCategory(
        categoryId: Int,
        pageNumber: Int
    ): PagedFetchResponse<TvShow>

    suspend fun incrementGenreInterest(genreId: Int, mediaType: String)
    suspend fun getGenreInterestCounts(mediaType: String): List<Pair<Int, Int>>
}
