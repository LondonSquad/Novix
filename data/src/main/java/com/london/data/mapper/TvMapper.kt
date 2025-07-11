package com.london.data.mapper

import com.london.data.datasource.local.model.SearchTvShowDtoLocal
import com.london.data.datasource.remote.search.SearchTvShowsResponseDto
import com.london.domain.entity.TvShow

fun SearchTvShowsResponseDto.toTvShowEntity(): TvShow {
    return TvShow(
            id = this.id,
            posterPicture = this.posterPath
        )
}

fun SearchTvShowDtoLocal.toTvShowEntity():TvShow{
    return TvShow(
            id = this.id,
            posterPicture = this.backdropPath
        )
}