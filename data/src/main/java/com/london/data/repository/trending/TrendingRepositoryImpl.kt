package com.london.data.repository.trending

import com.london.data.remote.source.home.trending.TrendingRemoteDataSource
import com.london.data.mapper.trending.toActor
import com.london.data.mapper.trending.toLocal
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

    suspend fun <T> Result<T?>.getNotNullOrElse(elseBlock: suspend () -> T): Result<T> =
        runCatching { getOrElse { elseBlock() } ?: elseBlock() }

    private suspend fun <T> fetchAndSync(
        networkBlock: suspend () -> T
    ): T = run { networkBlock() }

    override suspend fun getTrendingMovies(page: Int): PagedFetchResponse<Trending> {
        val response = fetchAndSync(
            networkBlock = {
                trendingRemoteDataSource.getTrendingMovies(page).body()?.toLocal(query = "") 
                    ?: throw Exception("Empty response")
            }
        )
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toTrending() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending> {
        val response = fetchAndSync(
            networkBlock = {
                trendingRemoteDataSource.getTrendingTvShows(page).body()?.toLocal(query = "") 
                    ?: throw Exception("Empty response")
            }
        )
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toTrending() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> {
        val response = fetchAndSync(
            networkBlock = {
                trendingRemoteDataSource.getTrendingActors(page).body()?.toLocal(query = "") 
                    ?: throw Exception("Empty response")
            }
        )
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toActor() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }
} 