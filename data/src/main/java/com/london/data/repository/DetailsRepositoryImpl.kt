package com.london.data.repository

import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.mapper.tvshowdetails.TvShowImagesMapper.toEntity
import com.london.data.mapper.tvshowdetails.toCastEntity
import com.london.data.mapper.tvshowdetails.toEntity
import com.london.domain.GetCastByIdFailedException
import com.london.domain.GetImagesByIdFailedException
import com.london.domain.TvShowDetailsSearchFailedException
import com.london.domain.entity.ActorDetails
import com.london.domain.entity.ActorMovieDetails
import com.london.domain.entity.ActorTvShowDetails
import com.london.domain.entity.tvshowdetails.CastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.repository.DetailsRepository

class DetailsRepositoryImpl(
    private val tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource
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

    override suspend fun getActorDetailsById(actorId: Int): ActorDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getActorMovieById(actorId: Int): List<ActorMovieDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun getActorTvShowById(actorId: Int): List<ActorTvShowDetails> {
        TODO("Not yet implemented")
    }
}