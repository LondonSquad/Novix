@file:KoverIgnore

package com.london.data.mapper.details.actor


import com.london.data.remote.model.details.actor.model.actorimage.ActorImageResponse
import com.london.data.remote.model.details.actor.model.actorimage.ProfileDto
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails

fun ActorImageResponse.toEntity(): ActorImageDetails {
    return ActorImageDetails(
        id = id.orZero(),
        profiles = profiles?.map { it.toEntity() }.orEmpty()
    )
}

fun ProfileDto.toEntity(): ImageDetails {
    return ImageDetails(
        fileUrl = filePath.asImageUrlOrEmpty(),
    )
}