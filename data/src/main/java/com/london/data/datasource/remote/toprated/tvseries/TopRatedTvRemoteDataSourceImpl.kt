@file:KoverIgnore

package com.london.data.datasource.remote.toprated.tvseries

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.toprated.tvseries.api.TopRatedTvSeriesApi
import com.london.data.datasource.remote.toprated.tvseries.model.TopRatedTvSeries
import com.london.domain.KoverIgnore
import org.koin.core.annotation.Single

@Single
class TopRatedTvRemoteDataSourceImpl(
    private val topRatedTvSeriesApi: TopRatedTvSeriesApi
) : TopRatedTvRemoteDataSource {
    override suspend fun getTopRatedTvShows(
        pageNumber: Int, language: String
    ): ApiResponse<TopRatedTvSeries> = topRatedTvSeriesApi.getTopRatedTvSeries(pageNumber, language)
}
