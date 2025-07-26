@file:KoverIgnore

package com.london.data.remote.service.toprated.tvseries

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.tvshow.model.TopRatedTvSeriesRemote
import com.london.domain.KoverIgnore
import retrofit2.http.GET
import retrofit2.http.Query

interface TopRatedTvSeriesApiService {

    @GET("3/tv/top_rated")
    suspend fun getTopRatedTvSeries(
        @Query("page") pageNumber: Int,
        @Query("language") language: String
    ): ApiResponse<TopRatedTvSeriesRemote>
}
