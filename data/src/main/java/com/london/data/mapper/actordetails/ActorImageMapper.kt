@file:KoverIgnore
package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ProfileDto
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails

fun ActorImageResponse.toEntity(): ActorImageDetails {
    return ActorImageDetails(
    id = this.id.orZero(),
    profiles = this.profiles?.map { it.toEntity() }.orEmpty()
    )
}

fun ProfileDto.toEntity(): ImageDetails {
    return ImageDetails(
        aspectRatio = this.aspectRatio.orZero(),
        height = this.height.orZero(),
        iso = this.iso,
        fileUrl = "https://image.tmdb.org/t/p/w500${this.filePath}",
        voteAverage = this.voteAverage.orZero(),
        voteCount = this.voteCount.orZero(),
        width = this.width.orZero()
    )
}