package com.london.domain.repository

import com.london.domain.entity.tvshowdetails.CastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity

interface DetailsRepository {
    suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsEntity
    suspend fun getCastTvShowById(tvShowId: Int): CastEntity
    suspend fun getImagesTvShowById(tvShowId: Int): TvShowImagesEntity
    suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int,
    ): TvShowEpisodesEntity

    suspend fun getTvShowEpisodeByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeByIdEntity
}
