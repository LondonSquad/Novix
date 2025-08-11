package com.london.domain.usecase.search

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class ManageSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend fun searchForActors(
        name: String,
        pageNumber: Int
    ) = repository.searchForActors(
        name = name,
        pageNumber = pageNumber
    )

    suspend fun searchForMovies(
        name: String,
        pageNumber: Int
    ) = repository.searchForMovies(
        name = name,
        pageNumber = pageNumber
    )

    suspend fun searchForTvShows(
        name: String,
        pageNumber: Int
    ) = repository.searchForTvShows(
        name = name,
        pageNumber = pageNumber
    )

    suspend fun incrementGenreInterest(genreId: Int, mediaType: String) =
        repository.incrementGenreInterest(genreId, mediaType)

    suspend fun getGenreInterestCounts(mediaType: String) =
        repository.getGenreInterestCounts(mediaType)

}