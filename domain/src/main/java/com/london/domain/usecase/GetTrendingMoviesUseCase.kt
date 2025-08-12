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
    suspend fun invoke(page: Int): PagedFetchResponse<Trending> =
        repository.getTrendingMovies(page)
}