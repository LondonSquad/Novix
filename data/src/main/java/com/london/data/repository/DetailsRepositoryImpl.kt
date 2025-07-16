package com.london.data.repository

import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.mapper.tvshowdetails.TvShowImagesMapper.toEntity
import com.london.data.mapper.tvshowdetails.toCastEntity
import com.london.data.mapper.tvshowdetails.toEntity
import com.london.domain.GetCastByIdFailedException
import com.london.domain.GetImagesByIdFailedException
import com.london.domain.TvShowDetailsSearchFailedException
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.repository.DetailsRepository

class DetailsRepositoryImpl(
    private val tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
) : DetailsRepository {
    override suspend fun getTvShowDetailsById(
        tvShowId: Int,
    ): TvShowDetailsEntity {
        return runCatching {
            tvShowDetailsRemoteDataSource.getTvShowDetailsById(
                tvShowId = tvShowId,
            ).toEntity()
        }.getOrElse {
            throw TvShowDetailsSearchFailedException()
        }
    }

    override suspend fun getCastTvShowById(tvShowId: Int): TvShowCastEntity {
        return runCatching {
            tvShowDetailsRemoteDataSource.getCastsByTvShowId(tvShowId).toCastEntity()
        }.getOrElse {
            throw GetCastByIdFailedException()
        }
    }

    override suspend fun getImagesTvShowById(tvShowId: Int): TvShowImagesEntity {
        return runCatching {
            tvShowDetailsRemoteDataSource.getTvShowImagesById(tvShowId).toEntity()
        }.getOrElse {
            throw GetImagesByIdFailedException()
        }
    }
}