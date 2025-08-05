package com.london.domain.usecase.movielist

import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class GetMovieListDetailsUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun invoke(listId: UInt, pageNumber: Int): PagedFetchResponse<Movie> =
        customMovieListRepository.getMovieListDetails(listId, pageNumber)
}
