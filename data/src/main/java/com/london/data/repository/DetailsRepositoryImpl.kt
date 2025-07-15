package com.london.data.repository

import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSource
import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.mapper.actordetails.toEntity
import com.london.data.mapper.tvshowdetails.TvShowImagesMapper.toEntity
import com.london.data.mapper.tvshowdetails.toCastEntity
import com.london.data.mapper.tvshowdetails.toEntity
import com.london.domain.ActorDetailsSearchFailedException
import com.london.domain.GetCastByIdFailedException
import com.london.domain.GetImagesByIdFailedException
import com.london.domain.TvShowDetailsSearchFailedException
import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.ActorImageDetails
import com.london.domain.entity.actordetails.ActorMovieDetails
import com.london.domain.entity.actordetails.ActorTvShowDetails
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.repository.DetailsRepository

class DetailsRepositoryImpl(
    private val tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
    private val actorDetailsRemoteDataSource: ActorDetailsRemoteDataSource
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

    override suspend fun getActorDetailsById(actorId: Int): ActorDetails {
        return runCatching {
            actorDetailsRemoteDataSource.getActorDetailsById(actorId).toEntity()
        }.getOrElse {
            throw ActorDetailsSearchFailedException()
        }
    }

    override suspend fun getActorMoviePicksById(actorId: Int): ActorMovieDetails {
        return runCatching {
            actorDetailsRemoteDataSource.getActorMovieById(actorId).toEntity()
        }.getOrElse {
            throw GetCastByIdFailedException()
        }
    }

    override suspend fun getActorTvShowPicksById(actorId: Int): ActorTvShowDetails {
        return runCatching {
            actorDetailsRemoteDataSource.getActorTvShowById(actorId).toEntity()
        }.getOrElse {
            throw GetCastByIdFailedException()
        }
    }

    override suspend fun getActorImagesById(actorId: Int): ActorImageDetails {
        return runCatching {
            actorDetailsRemoteDataSource.getActorImagePath(actorId).toEntity()
        }.getOrElse {
            throw GetImagesByIdFailedException()
        }
    }
}