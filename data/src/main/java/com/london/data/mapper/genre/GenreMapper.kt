package com.london.data.mapper.genre

import com.london.data.remote.model.details.movie.model.moviedetails.GenreRemote
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.Genre

@KoverIgnore
fun GenreRemote.toGenre(): Genre {
    return Genre(
        id = id.orZero(),
        name = name.orEmpty()
    )
}
