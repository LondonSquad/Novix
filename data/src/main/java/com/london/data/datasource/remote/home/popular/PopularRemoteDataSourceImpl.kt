package com.london.data.datasource.remote.home.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.api.PopularApiService
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import com.london.data.datasource.remote.home.popular.model.PopularTvShowResponse
import com.london.data.utils.safeCallApi
import org.koin.core.annotation.Single

@Single
class PopularRemoteDataSourceImpl(
    private val popularApiService: PopularApiService
) : PopularRemoteDataSource {
    override suspend fun getPopularMovies(): ApiResponse<PopularMovieResponse> {
        return safeCallApi { popularApiService.getPopularMovies() }
    }

    override suspend fun getPopularTvShows(): ApiResponse<PopularTvShowResponse> {
        return safeCallApi { popularApiService.getPopularTvShows() }
    }
}