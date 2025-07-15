package com.london.data.mapper.moviedetails

import com.london.data.datasource.remote.moviedetails.model.GenreRemote
import com.london.domain.entity.moviedatails.Genre

fun GenreRemote.toGenre(): Genre {
    return Genre(
        id = this.id, name = this.name
    )
}
