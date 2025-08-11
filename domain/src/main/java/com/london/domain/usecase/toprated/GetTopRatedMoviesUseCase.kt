package com.london.domain.usecase.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.repository.MovieRepository
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val topRatedMovieRepo: MovieRepository
) {
    suspend operator fun invoke(
        pageNumber: Int,
        genreId: Int? = null
    ): PagedFetchResponse<TopRatedMedia> {
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