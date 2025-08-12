package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTrendingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun invoke(page: Int, movieGenreId: Int?): PagedFetchResponse<Trending> {
        val trendingMovies = repository.getTrendingMovies(page)

        val genreId = if (movieGenreId == -1) null else movieGenreId

        val filteredItems = if (genreId != null)
            trendingMovies.items.filter { it.genreIds.contains(genreId) }
        else
            trendingMovies.items

        return PagedFetchResponse(
            currentPage = trendingMovies.currentPage,
            items = filteredItems,
            totalPages = trendingMovies.totalPages,
            totalItems = trendingMovies.totalItems,
        )
    }
}