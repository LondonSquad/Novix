package com.london.data.mapper.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.EpisodeCrewMember
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.EpisodeGuestStar
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeBySeason
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.domain.entity.Actor
import com.london.domain.entity.tvshowdetails.episode.EpisodeCrewMemberEntity
import com.london.domain.entity.tvshowdetails.episode.EpisodeGuestStarEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeBySeasonEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity

fun TvShowEpisodesRemoteResponse.toTvShowEpisodesEntity(): TvShowEpisodesEntity {
    return TvShowEpisodesEntity(
        id = this.id,
        airDate = this.airDate,
        episodes = this.episodes.map { it.toTvShowEpisodeBySeasonEntity() }
    )
}

fun TvShowEpisodeBySeason.toTvShowEpisodeBySeasonEntity(): TvShowEpisodeBySeasonEntity {
    return TvShowEpisodeBySeasonEntity(
        airDate = this.airDate,
        episodeNumber = this.episodeNumber,
        episodeType = this.episodeType,
        id = this.id,
        name = this.name,
        overview = this.overview,
        productionCode = this.productionCode,
        runtime = this.runtime,
        seasonNumber = this.seasonNumber,
        showId = this.showId,
        stillPath = "https://image.tmdb.org/t/p/w500${this.stillPath}",
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        crew = this.crew.map { it.toEpisodeCrewMemberEntity() },
        episodeGuestStars = this.episodeGuestStars.map { it.toEpisodeGuestStarEntity() }
    )
}

fun EpisodeCrewMember.toEpisodeCrewMemberEntity(): EpisodeCrewMemberEntity {
    return EpisodeCrewMemberEntity(
        job = this.job,
        department = this.department,
        creditId = this.creditId,
        adult = this.adult,
        gender = this.gender,
        id = this.id,
        knownForDepartment = this.knownForDepartment,
        name = this.name,
        originalName = this.originalName,
        popularity = this.popularity,
        profilePath = this.profilePath
    )
}

fun EpisodeGuestStar.toEpisodeGuestStarEntity(): EpisodeGuestStarEntity {
    return EpisodeGuestStarEntity(
        character = this.character,
        creditId = this.creditId,
        order = this.order,
        adult = this.adult,
        gender = this.gender,
        id = this.id,
        knownForDepartment = this.knownForDepartment,
        name = this.name,
        originalName = this.originalName,
        popularity = this.popularity,
        profilePath = this.profilePath
    )
}

fun TvShowEpisodeResponse.toTvShowEpisodeEntity(): TvShowEpisodeByIdEntity {
    return TvShowEpisodeByIdEntity(
        airDate = this.airDate,
        episodeNumber = this.episodeNumber,
        seasonNumber = this.seasonNumber,
        episodeTypes = this.episodeType,
        tvShowId = this.id,
        name = this.name,
        overview = this.overview,
        stillPath = this.stillPath,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        guestStars = this.guestStars.map { it.toActorEntity() },
        id = this.id
    )
}

fun EpisodeGuestStar.toActorEntity(): Actor {
    return Actor(
        id = this.id,
        name = this.name,
        profilePicture = this.profilePath ?: "",
        characterName = this.character
    )
}
