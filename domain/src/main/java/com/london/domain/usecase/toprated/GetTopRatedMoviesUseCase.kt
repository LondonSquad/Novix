package com.london.domain.usecase.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.repository.toprated.TopRatedMovieRepository
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val topRatedMovieRepo: TopRatedMovieRepository
) {
    suspend operator fun invoke(
        pageNumber: Int,
        genreId: Int? = null
    ): PagedFetchResponse<TopRatedMovie> {
        val response = topRatedMovieRepo.getTopRatedMovies(pageNumber)

        val filteredItems = response.items.filter { movie ->
            genreId == null || movie.genreIds.contains(genreId)
        }

        return response.copy(
            items = filteredItems,
            totalPages = filteredItems.size
        )
    }
}