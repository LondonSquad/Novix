package com.london.data.repository.home.upcoming

import com.london.data.local.source.home.upcoming.UpComingLocalDataSource
import com.london.data.mapper.toEntity
import com.london.data.mapper.toLocal
import com.london.data.remote.source.home.upcoming.UpComingRemoteDataSource
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.UpComingMovie
import com.london.domain.repository.UpComingRepository
import javax.inject.Inject

class UpComingRepositoryImpl @Inject constructor(
    private val upComingLocalDataSource: UpComingLocalDataSource,
    private val upComingRemoteDataSource: UpComingRemoteDataSource
) : UpComingRepository {
    override suspend fun getUpComingMoviesByCategory(
        categoryId: Int?, pageNumber: Int
    ): PagedFetchResponse<UpComingMovie> = fetchAndSync(
        cacheBlock = {
            upComingLocalDataSource.getUpComingMoviesPage(
                page = pageNumber,
                categoryId = categoryId
            )
        },
        syncBlock = { upComingLocalDataSource.insert(it) },
        networkBlock = {
            upComingRemoteDataSource.getUpComingMoviesByCategory(
                categoryId = categoryId,
                pageNumber = pageNumber,
            ).getOrThrow().toLocal(categoryId)
        }).run {
        PagedFetchResponse(
            currentPage = page,
            items = results.map { it.toEntity() },
            totalPages = totalPages,
            totalItems = totalResults
        )
    }
}