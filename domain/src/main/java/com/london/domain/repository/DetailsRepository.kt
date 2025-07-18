package com.london.domain.repository

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity

interface DetailsRepository {
    suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsEntity
    suspend fun getCastTvShowById(tvShowId: Int): TvShowCastEntity
    suspend fun getImagesTvShowById(tvShowId: Int): TvShowImagesEntity
    suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int,
    ): TvShowEpisodesEntity
    suspend fun getMovieReviews(movieId: Int, pageNumber: Int): PagedFetchResponse<ReviewEntity>
    suspend fun getTvShowReviews(tvShowId: Int, pageNumber: Int): PagedFetchResponse<ReviewEntity>
}