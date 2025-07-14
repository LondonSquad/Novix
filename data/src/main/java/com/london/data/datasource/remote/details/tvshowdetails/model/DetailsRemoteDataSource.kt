package com.london.data.datasource.remote.details.tvshowdetails.model

interface DetailsRemoteDataSource {
    suspend fun getTvSeriesDetailsById(
        tvShowId: Int,
        language: String,
    ): TvShowDetailsRemoteResponse
}