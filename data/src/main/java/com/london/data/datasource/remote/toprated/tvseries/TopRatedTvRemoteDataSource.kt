@file:KoverIgnore

package com.london.data.datasource.remote.toprated.tvseries

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.toprated.tvseries.model.TopRatedTvSeries
import com.london.domain.KoverIgnore

interface TopRatedTvRemoteDataSource {
    suspend fun getTopRatedTvShows(pageNumber: Int, language: String): ApiResponse<TopRatedTvSeries>
}