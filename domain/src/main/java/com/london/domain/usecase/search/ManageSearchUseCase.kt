package com.london.domain.usecase.search

import com.london.domain.entity.actor.Actor
import com.london.domain.entity.genre.Genre
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.entity.tvshow.TvShow
import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class ManageSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend fun searchForActors(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<Actor> = repository.searchForActors(
        name = name,
        pageNumber = pageNumber
    )

    suspend fun searchForMovies(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<Movie> = repository.searchForMovies(
        name = name,
        pageNumber = pageNumber
    )

    suspend fun searchForTvShows(
        name: String,
        pageNumber: Int
    ): PagedFetchResponse<TvShow> = repository.searchForTvShows(
        name = name,
        pageNumber = pageNumber
    )

    suspend fun incrementGenreInterest(genre: Genre, mediaType: String) =
        repository.incrementGenreInterest(genre, mediaType)

    suspend fun getGenreInterestCounts(mediaType: String): List<Pair<Int, Int>> =
        repository.getGenreInterestCounts(mediaType)

}
