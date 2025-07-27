@file:KoverIgnore

package com.london.data.remote.source.toprated.tvseries

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.TopRatedTvSeriesRemote
import com.london.data.remote.service.toprated.TopRatedTvSeriesApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.domain.KoverIgnore
import org.koin.core.annotation.Single

@Single
class TopRatedTvRemoteDataSourceImpl(
    private val topRatedTvSeriesApi: TopRatedTvSeriesApiService
) : TopRatedTvRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getTopRatedTvShows(
        pageNumber: Int,
    ): Result<ApiResponse<TopRatedTvSeriesRemote>> =
        callApi(
            apiCall = { topRatedTvSeriesApi.getTopRatedTvSeries(pageNumber) },
            mapper = { it }
        )
}