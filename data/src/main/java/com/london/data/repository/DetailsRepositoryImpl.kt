package com.london.data.repository

import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.datasource.remote.reviews.ReviewsRemoteDataSource
import com.london.data.datasource.util.CrashReporter
import com.london.data.mapper.toReviewEntity
import com.london.data.mapper.tvshowdetails.TvShowImagesMapper.toEntity
import com.london.data.mapper.tvshowdetails.toCastEntity
import com.london.data.mapper.tvshowdetails.toEntity
import com.london.data.mapper.tvshowdetails.toTvShowEpisodeEntity
import com.london.data.mapper.tvshowdetails.toTvShowEpisodesEntity
import com.london.domain.GetCastByIdFailedException
import com.london.domain.GetImagesByIdFailedException
import com.london.domain.TvShowDetailsSearchFailedException
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import com.london.domain.repository.DetailsRepository
import org.koin.core.annotation.Single

@Single
class DetailsRepositoryImpl(
    private val tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
    private val reviewsRemoteDataSource: ReviewsRemoteDataSource
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

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int
    ): TvShowEpisodesEntity {
        return runCatching {
            tvShowDetailsRemoteDataSource.getTvShowEpisodesBySeason(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber
            ).toTvShowEpisodesEntity()
        }.getOrElse {
            throw it
        }
    }

    override suspend fun getTvShowEpisodeByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeByIdEntity {
        return runCatching {
            tvShowDetailsRemoteDataSource.getEpisodeDetailsByPosition(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            ).toTvShowEpisodeEntity()
        }.getOrElse {
            throw it
        }
    }

    override suspend fun getMovieReviews(
        movieId: Int, pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> = fetchAndSync(
        networkBlock = {
            reviewsRemoteDataSource.getMovieReviews(
                movieId, pageNumber
            ).toReviewEntity()
        }
    ).run {
        PagedFetchResponse(
            currentPage = currentPage,
            items = items,
            totalPages = totalPages,
            totalItems = totalItems
        )
    }

    override suspend fun getTvShowReviews(
        tvShowId: Int, pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> = fetchAndSync(
        networkBlock = {
            reviewsRemoteDataSource.getTvShowReviews(tvShowId, pageNumber).toReviewEntity()
        }
    ).run {
        PagedFetchResponse(
            currentPage = currentPage, items = items, totalPages = totalPages, totalItems = totalItems
        )
    }

    suspend fun <T> Result<T?>.getNotNullOrElse(elseBlock: suspend () -> T): Result<T> =
        runCatching { getOrElse { elseBlock() } ?: elseBlock() }

    suspend fun <T> fetchAndSync(
        cacheBlock: (suspend () -> T?)? = null,
        networkBlock: suspend () -> T,
        syncBlock: (suspend (T) -> Unit)? = null,
        crashReporter: CrashReporter? = null
    ): T = runCatching { cacheBlock?.invoke() }.getNotNullOrElse {
        networkBlock().also {
            syncBlock?.invoke(it)
        }
    }.onFailure { crashReporter?.logException(it) }.getOrThrow()
}