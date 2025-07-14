package com.london.domain.repository

import com.london.domain.entity.CastEntity
import com.london.domain.entity.TvShowDetailsEntity
import com.london.domain.entity.TvShowImagesEntity

interface DetailsRepository {
    suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsEntity
    suspend fun getCastTvShowById(tvShowId: Int): CastEntity
    suspend fun getImagesTvShowById(tvShowId: Int): TvShowImagesEntity
}