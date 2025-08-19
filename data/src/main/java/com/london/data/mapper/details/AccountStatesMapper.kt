package com.london.data.mapper.details

import com.london.data.remote.model.account.AccountStatesResponse
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.parseRatingValue
import com.london.domain.entity.shared.MediaStates

fun AccountStatesResponse.toEntity() = MediaStates(
    favorite = favorite.isTrue,
    id = id.orZero(),
    rate  = rated.parseRatingValue()?.toInt().orZero(),
    watchlist = watchlist.isTrue
)
