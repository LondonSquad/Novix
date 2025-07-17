@file:KoverIgnore

package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ProfileDto
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails

fun ActorImageResponse.toEntity(): ActorImageDetails {
    return ActorImageDetails(
        id = id, profiles = profiles.map { it.toEntity() })
}

fun ProfileDto.toEntity(): ImageDetails {
    return ImageDetails(
        aspectRatio = aspectRatio,
        height = height,
        iso = iso,
        fileUrl = filePath.asImageUrlOrEmpty(),
        voteAverage = voteAverage,
        voteCount = voteCount,
        width = width
    )
}
