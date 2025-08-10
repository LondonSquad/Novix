@file:KoverIgnore

package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeGuestStar
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeBySeason
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.Actor
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeBySeasonEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity

@KoverIgnore
fun TvShowEpisodesRemoteResponse.toTvShowEpisodesEntity(): TvShowEpisodesEntity =
    TvShowEpisodesEntity(
        id = id.orEmpty(),
        episodes = episodes?.map { it.toTvShowEpisodeBySeasonEntity() }.orEmpty()
    )

fun TvShowEpisodeBySeason.toTvShowEpisodeBySeasonEntity(): TvShowEpisodeBySeasonEntity =
    TvShowEpisodeBySeasonEntity(
        airDate = airDate,
        episodeNumber = episodeNumber.orZero(),
        episodeType = episodeType.orEmpty(),
        id = id.orZero(),
        name = name.orEmpty(),
        overview = overview.orEmpty(),
        runtime = runtime,
        seasonNumber = seasonNumber.orZero(),
        showId = showId.orZero(),
        imageUrl = stillPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage.orZero().roundToDecimal(),
    )

@KoverIgnore
fun TvShowEpisodeResponse.toTvShowEpisodeEntity(): TvShowEpisodeByIdEntity =
    TvShowEpisodeByIdEntity(
        airDate = airDate,
        seasonNumber = seasonNumber.orZero(),
        episodeTypes = episodeType.orEmpty(),
        tvShowId = id.orZero(),
        name = name.orEmpty(),
        overview = overview.orEmpty(),
        imageUrl = stillPath.orEmpty(),
        voteAverage = voteAverage.orZero().roundToDecimal(),
        voteCount = voteCount.orZero(),
        guestStars = guestStars?.map { it.toActorEntity() }.orEmpty(),
        id = id.orZero()
    )

fun EpisodeGuestStar.toActorEntity(): Actor =
    Actor(
        id = id.orZero(),
        name = name.orEmpty(),
        profilePictureUrl = profilePath.asImageUrlOrEmpty(),
        characterName = character.orEmpty()
    )

