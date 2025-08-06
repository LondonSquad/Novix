package com.london.domain.repository

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity

interface TvShowRepository {
    suspend fun getTvShowDetailsById(id: Int): TvShowDetailsEntity
    suspend fun getCastTvShowById(id: Int): TvShowCastEntity
    suspend fun getImagesTvShowById(id: Int): TvShowImagesEntity
    suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int,
    ): TvShowEpisodesEntity

    suspend fun getTvShowEpisodeByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeByIdEntity

    suspend fun getEpisodeVideos(seriesId: Int, seasonNumber: Int, episodeNumber: Int): List<String>

    suspend fun getTvShowReviews(tvShowId: Int, pageNumber: Int): PagedFetchResponse<ReviewEntity>
}