package com.london.domain.entity.recent

enum class MediaType {
    TvShow,
    Movie;

    companion object {
        fun MediaType.isMovie() = this == Movie
        fun MediaType.isTvShow() = this == TvShow
    }
}
