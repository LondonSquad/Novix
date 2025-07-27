@file:KoverIgnore

package com.london.data.remote.source.toprated.tvseries

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.tvshow.model.TopRatedTvSeriesRemote
import com.london.domain.KoverIgnore

interface TopRatedTvRemoteDataSource {
    suspend fun getTopRatedTvShows(
        pageNumber: Int,
        language: String
    ): Result<ApiResponse<TopRatedTvSeriesRemote>>
}