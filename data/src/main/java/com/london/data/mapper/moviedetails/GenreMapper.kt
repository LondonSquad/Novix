package com.london.data.mapper.moviedetails

import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.GenreRemote
import com.london.domain.KoverIgnore
import com.london.domain.entity.moviedatails.Genre

@KoverIgnore
fun GenreRemote.toGenre(): Genre {
    return Genre(
        id = this.id, name = this.name
    )
}
