package com.london.data.repository.trending

import com.london.data.mapper.trending.toEntityActor
import com.london.data.mapper.trending.toEntityMedia
import com.london.data.remote.source.home.trending.TrendingRemoteDataSource
import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.TrendingRepository
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(
    private val trendingRemoteDataSource: TrendingRemoteDataSource
) : TrendingRepository {

    override suspend fun getTrendingMovies(page: Int): PagedFetchResponse<Trending> {
        val response = trendingRemoteDataSource.getTrendingMovies(page).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntityMedia() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending> {
        val response = trendingRemoteDataSource.getTrendingTvShows(page).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntityMedia() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> {
        val response = trendingRemoteDataSource.getTrendingActors(page).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntityActor() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }
}