package com.london.data.repository

import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.mapper.TvShowImagesMapper.toEntity
import com.london.data.mapper.toCastEntity
import com.london.data.mapper.toEntity
import com.london.domain.GetCastByIdFailedException
import com.london.domain.GetImagesByIdFailedException
import com.london.domain.TvShowDetailsSearchFailedException
import com.london.domain.entity.CastEntity
import com.london.domain.entity.TvShowDetailsEntity
import com.london.domain.entity.TvShowImagesEntity
import com.london.domain.repository.DetailsRepository

class DetailsRepositoryImpl(
    private val tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource
) : DetailsRepository {
    override suspend fun getTvShowDetailsById(
        tvShowId: Int,
        language: String
    ): TvShowDetailsEntity {
        return runCatching {
            tvShowDetailsRemoteDataSource.getTvShowDetailsById(
                tvShowId = tvShowId,
                language = language
            ).toEntity()
        }.getOrElse {
            throw TvShowDetailsSearchFailedException()
        }
    }

    override suspend fun getCastTvShowById(tvShowId: Int): CastEntity {
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