@file:KoverIgnore

package com.london.data.remote.source.toprated.tvseries

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.toprated.tvseries.api.TopRatedTvSeriesApiService
import com.london.data.datasource.remote.toprated.tvseries.model.TopRatedTvSeriesRemote
import com.london.domain.KoverIgnore
import org.koin.core.annotation.Single

@Single
class TopRatedTvRemoteDataSourceImpl(
    private val topRatedTvSeriesApi: TopRatedTvSeriesApiService
) : TopRatedTvRemoteDataSource {
    override suspend fun getTopRatedTvShows(
        pageNumber: Int,
    ): ApiResponse<TopRatedTvSeriesRemote> =
        topRatedTvSeriesApi.getTopRatedTvSeries(pageNumber)
}
