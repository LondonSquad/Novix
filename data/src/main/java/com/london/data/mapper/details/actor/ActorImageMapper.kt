@file:KoverIgnore

package com.london.data.mapper.details.actor


import com.london.data.remote.model.details.actor.model.actorimage.ActorImageResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.ActorImageDetails

fun ActorImageResponse.toEntity(): ActorImageDetails =
    ActorImageDetails(
        id = id.orZero(),
        imageUrl = profiles?.map { it.filePath.asImageUrlOrEmpty() }.orEmpty()
    )