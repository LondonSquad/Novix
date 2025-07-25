package com.london.data.repository

import com.london.data.datasource.remote.home.trending.TrendingRemoteDataSource
import com.london.data.mapper.toDomain
import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.TrendingActorsRepository
import org.koin.core.annotation.Single

@Single
class TrendingActorsRepositoryImpl(
    private val remoteDataSource: TrendingRemoteDataSource
) : TrendingActorsRepository {
    override suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> {
        val response = remoteDataSource.getTrendingActors(page)
        val actors = response.results.map { it.toDomain() }
        return PagedFetchResponse(
            currentPage = response.page,
            items = actors,
            totalPages = response.totalPages,
            totalItems = actors.size
        )
    }
}