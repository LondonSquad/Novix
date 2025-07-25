package com.london.data.repository.trending

import com.london.data.datasource.remote.home.trending.TrendingRemoteDataSource
import com.london.data.mapper.trending.toTrendingMovie
import com.london.domain.entity.trending.TrendingMovie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.TrendingRepository
import org.koin.core.annotation.Single

@Single
class TrendingMovieRepositoryImpl(
    private val trendingRemoteDataSource: TrendingRemoteDataSource
) : TrendingRepository {
    override suspend fun getTrendingMovies(page: Int): PagedFetchResponse<TrendingMovie> {
        val response = trendingRemoteDataSource.getTrendingMovies(page)
        val movies = response.results.map { it.toTrendingMovie() }
        return PagedFetchResponse(
            currentPage = response.page,
            items = movies,
            totalPages = response.totalPages,
            totalItems = movies.size
        )
    }
} 