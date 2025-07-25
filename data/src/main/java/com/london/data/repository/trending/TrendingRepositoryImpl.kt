package com.london.data.repository.trending

import com.london.data.remote.source.home.trending.TrendingRemoteDataSource
import com.london.data.mapper.trending.toActor
import com.london.data.mapper.trending.toTrending
import com.london.domain.entity.trending.Trending
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Actor
import com.london.domain.repository.TrendingRepository
import org.koin.core.annotation.Single

@Single
class TrendingRepositoryImpl(
    private val trendingRemoteDataSource: TrendingRemoteDataSource
) : TrendingRepository {

    override suspend fun getTrendingMovies(page: Int): PagedFetchResponse<Trending> {
        val response = trendingRemoteDataSource.getTrendingMovies(page)
        val movies = response.results.map { it.toTrending() }
        return PagedFetchResponse(
            currentPage = response.page,
            items = movies,
            totalPages = response.totalPages,
            totalItems = movies.size
        )
    }

    override suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending> {
        val response = trendingRemoteDataSource.getTrendingTvShows(page)
        val tvShows = response.results.map { it.toTrending() }
        return PagedFetchResponse(
            currentPage = response.page,
            items = tvShows,
            totalPages = response.totalPages,
            totalItems = tvShows.size
        )
    }

    override suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> {
        val response = trendingRemoteDataSource.getTrendingActors(page)
        val actors = response.results.map { it.toActor() }
        return PagedFetchResponse(
            currentPage = response.page,
            items = actors,
            totalPages = response.totalPages,
            totalItems = actors.size
        )
    }
} 