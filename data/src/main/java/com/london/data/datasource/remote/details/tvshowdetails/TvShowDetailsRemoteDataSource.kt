package com.london.data.datasource.remote.details.tvshowdetails

import com.london.data.datasource.remote.cast.model.CastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse

interface TvShowDetailsRemoteDataSource {
    suspend fun getTvShowDetailsById(
        tvShowId: Int,
        language: String,
    ): TvShowDetailsRemoteResponse

    suspend fun getCastsByTvShowId(tvShowId: Int): CastRemoteResponse

    suspend fun getTvShowImagesById(tvShowId: Int): TvShowImagesRemoteResponse
}