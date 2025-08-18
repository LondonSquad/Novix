package com.london.domain.usecase.movielist

import com.london.domain.entity.movie.Movie
import com.london.domain.entity.movie.MovieList
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class ManageGetMovieUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {
    suspend fun getAllMovieLists(pageNumber: Int): PagedFetchResponse<MovieList> =
        customMovieListRepository.getMovieLists(pageNumber = pageNumber)

    suspend fun getMovieListDetails(listId: Int, pageNumber: Int): PagedFetchResponse<Movie> =
        customMovieListRepository.getMovieListDetails(listId, pageNumber)
}