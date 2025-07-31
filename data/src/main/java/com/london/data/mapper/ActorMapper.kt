@file:KoverIgnore

package com.london.data.mapper

import com.london.data.local.model.search.ActorLocal
import com.london.data.local.model.search.SearchActorsLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.searchactormodel.SearchActorRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.generateHash
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.Actor

fun ActorLocal.toEntity(
    characterName: String,
) = Actor(
    id = id.orZero(),
    name = name,
    profilePictureUrl = profilePicture.asImageUrlOrEmpty(),
    characterName = characterName.orEmpty()
)

fun ApiResponse<SearchActorRemote>.toLocal(query: String) = SearchActorsLocal(
    date = System.currentTimeMillis(),
    query = query.generateHash(),
    page = currentPage.orZero(),
    results = items.map { it.toLocal() },
    totalPages = totalPages,
    totalResults = totalItems
)

fun SearchActorRemote.toLocal() = ActorLocal(
    id = id.orZero(),
    name = name.orEmpty(),
    profilePicture = profilePath.orEmpty(),
)
