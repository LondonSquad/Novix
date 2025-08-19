package com.london.domain.entity.tvshow

import com.london.domain.entity.genre.TvShowGenre

data class TvShow(
    val id: Int,
    val name: String,
    val posterPicture: String,
    val releaseYear: Int,
    val rating: Int,
    val genres: List<TvShowGenre>
)
