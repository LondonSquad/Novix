package com.london.data.mapper.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.EpisodeCrewMember
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.EpisodeGuestStar
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeBySeason
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.KoverIgnore
import com.london.domain.entity.Actor
import com.london.domain.entity.tvshowdetails.episode.EpisodeCrewMemberEntity
import com.london.domain.entity.tvshowdetails.episode.EpisodeGuestStarEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeBySeasonEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity


@KoverIgnore
fun TvShowEpisodesRemoteResponse.toTvShowEpisodesEntity(): TvShowEpisodesEntity {
    return TvShowEpisodesEntity(
        id = id,
        airDate = airDate,
        episodes = episodes.map { it.toTvShowEpisodeBySeasonEntity() }
    )
}


fun TvShowEpisodeBySeason.toTvShowEpisodeBySeasonEntity(): TvShowEpisodeBySeasonEntity {
    return TvShowEpisodeBySeasonEntity(
        airDate = airDate,
        episodeNumber = episodeNumber,
        episodeType = episodeType,
        id = id,
        name = name,
        overview = overview,
        productionCode = productionCode,
        runtime = runtime,
        seasonNumber = seasonNumber,
        showId = showId,
        stillUrl = stillPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage,
        voteCount = voteCount,
        crew = crew.map { it.toEpisodeCrewMemberEntity() },
        episodeGuestStars = episodeGuestStars.map { it.toEpisodeGuestStarEntity() }
    )
}

@KoverIgnore
fun EpisodeCrewMember.toEpisodeCrewMemberEntity(): EpisodeCrewMemberEntity {
    return EpisodeCrewMemberEntity(
        job = job,
        department = department,
        creditId = creditId,
        adult = adult,
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        originalName = originalName,
        popularity = popularity,
        profilePath = profilePath.asImageUrlOrEmpty()
    )
}

@KoverIgnore
fun EpisodeGuestStar.toEpisodeGuestStarEntity(): EpisodeGuestStarEntity {
    return EpisodeGuestStarEntity(
        character = character,
        creditId = creditId,
        order = order,
        adult = adult,
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        originalName = originalName,
        popularity = popularity,
        profilePath = profilePath.asImageUrlOrEmpty()
    )
}

@KoverIgnore
fun TvShowEpisodeResponse.toTvShowEpisodeEntity(): TvShowEpisodeByIdEntity {
    return TvShowEpisodeByIdEntity(
        airDate = this.airDate,
        episodeNumber = this.episodeNumber?:0,
        seasonNumber = this.seasonNumber?:0,
        episodeTypes = this.episodeType?:"",
        tvShowId = this.id?:0,
        name = this.name?:"",
        overview = this.overview?:"",
        stillPath = this.stillPath?:"",
        voteAverage = this.voteAverage?:0.0,
        voteCount = this.voteCount?:0,
        guestStars = this.guestStars?.map { it.toActorEntity() } ?: emptyList(),
        id = this.id?:0
    )
}

fun EpisodeGuestStar.toActorEntity(): Actor {
    return Actor(
        id = this.id,
        name = this.name,
        profilePicture = this.profilePath.asImageUrlOrEmpty(),
        characterName = this.character
    )
}
