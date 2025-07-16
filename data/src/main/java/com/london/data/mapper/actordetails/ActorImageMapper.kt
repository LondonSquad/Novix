package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ProfileDto
import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails

fun ActorImageResponse.toEntity(): ActorImageDetails {
    return ActorImageDetails(
    id = this.id,
    profiles = this.profiles.map { it.toEntity() }
    )
}

fun ProfileDto.toEntity(): ImageDetails {
    return ImageDetails(
        aspectRatio = this.aspectRatio,
        height = this.height,
        iso = this.iso,
        filePath = "https://image.tmdb.org/t/p/w500${this.filePath}",
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        width = this.width
    )
}