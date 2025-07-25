package com.london.data.remote.source.home.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.BaseRemoteDatasource
import com.london.data.remote.service.home.popular.PopularApiService
import com.london.data.remote.model.home.model.PopularMovieResponse
import com.london.data.remote.model.home.model.PopularTvShowResponse
import org.koin.core.annotation.Single

@Single
class PopularRemoteDataSourceImpl(
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