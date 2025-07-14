package com.london.domain.repository

import com.london.domain.entity.tvshowdetails.CastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity

interface DetailsRepository {
    suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsEntity
    suspend fun getCastTvShowById(tvShowId: Int): CastEntity
    suspend fun getImagesTvShowById(tvShowId: Int): TvShowImagesEntity
}