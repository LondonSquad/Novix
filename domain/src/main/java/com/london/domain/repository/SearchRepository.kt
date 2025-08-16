package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.Genre

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

    suspend fun incrementGenreInterest(genre: Genre, mediaType: String)
    suspend fun getGenreInterestCounts(mediaType: String): List<Pair<Int, Int>>
}
