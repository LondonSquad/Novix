package com.london.presentation.shared.container

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow

fun <T : Any> T.getName(): String {
    return when (this) {
        is Movie -> this.name
        is TvShow -> this.name
        else -> toString()
    }
}

fun <T : Any> T.getImageUrl(): String {
    return when (this) {
        is Movie -> this.posterUrl
        is TvShow -> this.posterPicture
        else -> ""
    }
}
