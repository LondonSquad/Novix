package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.ProfileDto
import com.london.domain.entity.actordetails.ActorImageDetails
import com.london.domain.entity.actordetails.ImageDetails

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
        filePath = this.filePath,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        width = this.width
    )
}