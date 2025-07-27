@file:KoverIgnore

package com.london.data.remote.source.toprated.tvseries

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.tvshow.model.TopRatedTvSeriesRemote
import com.london.data.remote.service.toprated.tvseries.TopRatedTvSeriesApiService
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
