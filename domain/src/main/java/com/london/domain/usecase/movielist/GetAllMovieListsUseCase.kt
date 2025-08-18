package com.london.domain.usecase.movielist

import com.london.domain.entity.movie.MovieList
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class GetAllMovieListsUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun invoke(pageNumber: Int): PagedFetchResponse<MovieList> =
        customMovieListRepository.getMovieLists(pageNumber = pageNumber)
}
