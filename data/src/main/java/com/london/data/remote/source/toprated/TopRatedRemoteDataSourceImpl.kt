@file:KoverIgnore

package com.london.data.remote.source.toprated

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.toprated.TopRatedMovieRemote
import com.london.data.remote.model.home.toprated.TopRatedTvSeriesRemote
import com.london.data.remote.service.toprated.TopRatedApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.domain.KoverIgnore
import javax.inject.Inject

class TopRatedRemoteDataSourceImpl @Inject constructor(
    private val topRatedMovieApi: TopRatedApiService
) : TopRatedRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): Result<ApiResponse<TopRatedMovieRemote>> =
        callApiWithRetry(
            apiCall = { topRatedMovieApi.getTopRatedMovies(pageNumber) },
            mapper = { it }
        )

    override suspend fun getTopRatedTvShows(pageNumber: Int): Result<ApiResponse<TopRatedTvSeriesRemote>> =
        callApi(
            apiCall = { topRatedMovieApi.getTopRatedTvSeries(pageNumber) },
            mapper = { it }
        )
}