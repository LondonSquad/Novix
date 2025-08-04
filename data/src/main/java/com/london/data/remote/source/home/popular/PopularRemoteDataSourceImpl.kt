package com.london.data.remote.source.home.popular

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.popular.PopularMovieResponse
import com.london.data.remote.model.home.popular.PopularTvShowResponse
import com.london.data.remote.service.home.PopularApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject


class PopularRemoteDataSourceImpl @Inject constructor(
    private val popularApiService: PopularApiService
) : PopularRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getPopularMovies(): Result<ApiResponse<PopularMovieResponse>> {
        return callApiWithRetry(
            apiCall = { popularApiService.getPopularMovies() },
            mapper = { it }
        )
    }

    override suspend fun getPopularTvShows(): Result<ApiResponse<PopularTvShowResponse>> {
        return callApiWithRetry(
            { popularApiService.getPopularTvShows() },
            mapper = { it }
        )
    }
}