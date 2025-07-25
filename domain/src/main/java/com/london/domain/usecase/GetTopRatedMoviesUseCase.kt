package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.repository.TopRatedMovieRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTopRatedMoviesUseCase(
    @Provided
    private val topRatedMovieRepo: TopRatedMovieRepository
) {
    suspend fun invoke(
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
