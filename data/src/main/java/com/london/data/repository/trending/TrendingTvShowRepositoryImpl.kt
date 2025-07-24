package com.london.data.repository.trending

import com.london.data.datasource.remote.home.trending.TrendingRemoteDataSource
import com.london.data.mapper.trending.toTrendingTvShow
import com.london.domain.entity.trending.TrendingTvShow
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.TrendingTvShowRepository
import org.koin.core.annotation.Single

@Single
class TrendingTvShowRepositoryImpl(
    private val trendingRemoteDataSource: TrendingRemoteDataSource
) : TrendingTvShowRepository {
    override suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<TrendingTvShow> {
        val response = trendingRemoteDataSource.getTrendingTvShows(page)
        val tvShows = response.results.map { it.toTrendingTvShow() }
        return PagedFetchResponse(
            currentPage = response.page,
            items = tvShows,
            totalPages = response.totalPages,
            totalItems = tvShows.size
        )
    }
} 