package com.london.data.remote.source.home.upcoming

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.MovieRemote
import com.london.data.remote.service.home.UpComingApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.data.utils.getCurrentDate
import javax.inject.Inject

class UpComingRemoteDataSourceImpl @Inject constructor(
    private val upComingApiService: UpComingApiService
): UpComingRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getUpComingMoviesByCategory(
        categoryId: Int?,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<MovieRemote>> =
        callApiWithRetry(
            {
                upComingApiService.getUpComingMoviesByCategory(
                    genreId = categoryId,
                    releaseDate = getCurrentDate(),
                    page = pageNumber,
                    includeAdult = includeAdult
                )
            },
            mapper = { it }
        )
}