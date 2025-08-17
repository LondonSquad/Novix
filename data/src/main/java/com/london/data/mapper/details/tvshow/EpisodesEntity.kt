package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeBySeason
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeGuestStar
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodesRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.entity.Actor
import com.london.domain.entity.tvshowdetails.episode.EpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.EpisodeBySeasonEntity
import com.london.domain.entity.tvshowdetails.episode.EpisodesEntity

fun EpisodesRemoteResponse.toEpisodesEntity(): EpisodesEntity =
    EpisodesEntity(
        id = id.orEmpty(),
        episodes = episodes.orEmpty().map { it.toEpisodeBySeasonEntity() }
    )

fun EpisodeBySeason.toEpisodeBySeasonEntity(): EpisodeBySeasonEntity =
    EpisodeBySeasonEntity(
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

fun EpisodeResponse.toEpisodeEntity(): EpisodeByIdEntity =
    EpisodeByIdEntity(
        airDate = airDate,
        seasonNumber = seasonNumber.orZero(),
        episodeTypes = episodeType.orEmpty(),
        tvShowId = id.orZero(),
        name = name.orEmpty(),
        overview = overview.orEmpty(),
        imageUrl = stillPath.orEmpty(),
        voteAverage = voteAverage.orZero().roundToDecimal(),
        voteCount = voteCount.orZero(),
        guestStars = guestStars.orEmpty().map { it.toActorEntity() },
        id = id.orZero()
    )

fun EpisodeGuestStar.toActorEntity(): Actor =
    Actor(
        id = id.orZero(),
        name = name.orEmpty(),
        profilePictureUrl = profilePath.asImageUrlOrEmpty(),
        characterName = character.orEmpty()
    )

