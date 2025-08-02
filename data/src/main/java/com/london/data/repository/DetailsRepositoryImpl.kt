package com.london.data.repository

import com.london.data.mapper.toReviewEntity
import com.london.data.mapper.tvshowdetails.TvShowImagesMapper.toEntity
import com.london.data.mapper.tvshowdetails.toCastEntity
import com.london.data.mapper.tvshowdetails.toEntity
import com.london.data.mapper.tvshowdetails.toTvShowEpisodeEntity
import com.london.data.mapper.tvshowdetails.toTvShowEpisodesEntity
import com.london.data.remote.source.details.tvshow.TvShowDetailsRemoteDataSource
import com.london.data.remote.source.reviews.ReviewsRemoteDataSource
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import com.london.domain.repository.DetailsRepository
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
    private val reviewsRemoteDataSource: ReviewsRemoteDataSource
) : DetailsRepository {
    override suspend fun getTvShowDetailsById(
        id: Int,
    ): TvShowDetailsEntity = tvShowDetailsRemoteDataSource.getTvShowDetailsById(
        id = id,
    ).getOrThrow().toEntity()

    override suspend fun getCastTvShowById(id: Int): TvShowCastEntity =
        tvShowDetailsRemoteDataSource.getCastsByTvShowId(id).getOrThrow().toCastEntity()

    override suspend fun getImagesTvShowById(id: Int): TvShowImagesEntity =
        tvShowDetailsRemoteDataSource.getTvShowImagesById(id).getOrThrow().toEntity()

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int, seasonNumber: Int
    ): TvShowEpisodesEntity =

        tvShowDetailsRemoteDataSource.getTvShowEpisodesBySeason(
            id = tvShowId, seasonNumber = seasonNumber
        ).getOrThrow().toTvShowEpisodesEntity()


    override suspend fun getTvShowEpisodeByPosition(
        tvShowId: Int, seasonNumber: Int, episodeNumber: Int
    ): TvShowEpisodeByIdEntity =
        tvShowDetailsRemoteDataSource.getEpisodeDetailsByPosition(
            tvShowId = tvShowId, seasonNumber = seasonNumber, episodeNumber = episodeNumber
        ).getOrThrow().toTvShowEpisodeEntity()

    override suspend fun getEpisodeVideos(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<String> =
        tvShowDetailsRemoteDataSource.getEpisodeVideos(
            seriesId = seriesId, seasonNumber = seasonNumber, episodeNumber = episodeNumber
        ).getOrThrow().results?.map { it.key.asYoutubeUrlOrEmpty() }.orEmpty()


    override suspend fun getMovieReviews(
        movieId: Int, pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> = fetchAndSync(
        networkBlock = {
            reviewsRemoteDataSource.getMovieReviews(
                movieId, pageNumber
            ).getOrThrow().toReviewEntity()
        }).run {
        PagedFetchResponse(
            currentPage = currentPage,
            items = items,
            totalPages = totalPages,
            totalItems = totalItems
        )
    }

    override suspend fun getTvShowReviews(
        tvShowId: Int,
        pageNumber: Int
    ): PagedFetchResponse<ReviewEntity> = fetchAndSync(
        networkBlock = {
            reviewsRemoteDataSource.getTvShowReviews(tvShowId, pageNumber).getOrThrow()
                .toReviewEntity()
        }).run {
        PagedFetchResponse(
            currentPage = currentPage,
            items = items,
            totalPages = totalPages,
            totalItems = totalItems
        )
    }
}