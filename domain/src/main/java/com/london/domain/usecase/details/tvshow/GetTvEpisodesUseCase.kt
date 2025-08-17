package com.london.domain.usecase.details.tvshow

import com.london.domain.entity.tvshowdetails.episode.EpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.EpisodesEntity
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTvEpisodesUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend fun getEpisodeByTvShowId(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodeByIdEntity =
        tvShowRepository.getTvShowEpisodeByPosition(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )

    suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int
    ): EpisodesEntity =
        tvShowRepository.getTvShowEpisodesBySeason(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber
        )

    suspend fun getEpisodeVideos(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<String> =
        tvShowRepository.getEpisodeVideos(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )
}
