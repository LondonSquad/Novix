@file:KoverIgnore

package com.london.data.mapper.search

import com.london.data.remote.model.search.SearchActorRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actor.Actor

fun SearchActorRemote.toEntity() = Actor(
    id = id.orZero(),
    name = name.orEmpty(),
    profilePictureUrl = profilePath.asImageUrlOrEmpty(),
    characterName = originalName.orEmpty(),
)
