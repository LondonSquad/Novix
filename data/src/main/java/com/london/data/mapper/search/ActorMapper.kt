@file:KoverIgnore

package com.london.data.mapper.search

import com.london.data.remote.model.search.searchactor.SearchActorRemote
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.Actor

fun SearchActorRemote.toEntity() = Actor(
    id = id.orZero(),
    name = name.orEmpty(),
    profilePictureUrl = profilePath.orEmpty(),
    characterName = originalName.orEmpty(),
)