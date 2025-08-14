package com.london.data.mapper.genre

import com.london.domain.entity.genre.Genre
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.entity.recent.MediaType

fun Int.toMovieGenre(): MovieGenre = when (this) {
    28 -> MovieGenre.ACTION
    12 -> MovieGenre.ADVENTURE
    16 -> MovieGenre.ANIMATION
    35 -> MovieGenre.COMEDY
    80 -> MovieGenre.CRIME
    99 -> MovieGenre.DOCUMENTARY
    18 -> MovieGenre.DRAMA
    10751 -> MovieGenre.FAMILY
    14 -> MovieGenre.FANTASY
    36 -> MovieGenre.HISTORY
    27 -> MovieGenre.HORROR
    10402 -> MovieGenre.MUSIC
    9648 -> MovieGenre.MYSTERY
    10749 -> MovieGenre.ROMANCE
    878 -> MovieGenre.SC_IFI
    10770 -> MovieGenre.TV_MOVIE
    53 -> MovieGenre.THRILLER
    10752 -> MovieGenre.WAR
    37 -> MovieGenre.WESTERN
    else -> MovieGenre.UNKNOWN
}

fun Int.toTvShowGenre(): TvShowGenre = when (this) {
    10759 -> TvShowGenre.ACTION_ADVENTURE
    16 -> TvShowGenre.ANIMATION
    35 -> TvShowGenre.COMEDY
    80 -> TvShowGenre.CRIME
    99 -> TvShowGenre.DOCUMENTARY
    18 -> TvShowGenre.DRAMA
    10751 -> TvShowGenre.FAMILY
    10762 -> TvShowGenre.KIDS
    9648 -> TvShowGenre.MYSTERY
    10763 -> TvShowGenre.NEWS
    10764 -> TvShowGenre.REALITY
    10765 -> TvShowGenre.FANTASY
    10766 -> TvShowGenre.SOAP
    10767 -> TvShowGenre.TALK
    10768 -> TvShowGenre.WAR_POLITICS
    37 -> TvShowGenre.WESTERN
    else -> TvShowGenre.UNKNOWN
}

fun Int.toGenre(mediaType: MediaType): Genre = when (mediaType) {
    MediaType.Movie -> this.toMovieGenre()
    MediaType.TvShow -> this.toTvShowGenre()
}
