package com.london.data.remote.source.home.upcoming

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.MovieRemote

interface UpComingRemoteDataSource {
    suspend fun getUpComingMoviesByCategory(
        categoryId: Int?=null,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): Result<ApiResponse<MovieRemote>>
}