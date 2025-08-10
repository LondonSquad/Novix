package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.TrendingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTrendingMoviesUseCase @Inject constructor(
    private val repository: TrendingRepository
) {
    suspend fun invoke(page: Int, genreId: Int? = null): PagedFetchResponse<Trending> {
        val movies = repository.getTrendingMovies(page)
        
        return if (genreId != null && genreId != -1) {
            val filteredItems = movies.items.filter { it.genreIds.contains(genreId) }
            movies.copy(items = filteredItems)
        } else {
            movies
        }
    }
}