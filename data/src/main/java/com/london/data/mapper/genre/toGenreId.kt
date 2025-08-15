package com.london.data.mapper.genre

import com.london.domain.entity.genre.Genre
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.genre.TvShowGenre

fun MovieGenre.getId(): Int = when (this) {
    MovieGenre.ACTION -> 28
    MovieGenre.ADVENTURE -> 12
    MovieGenre.ANIMATION -> 16
    MovieGenre.COMEDY -> 35
    MovieGenre.CRIME -> 80
    MovieGenre.DOCUMENTARY -> 99
    MovieGenre.DRAMA -> 18
    MovieGenre.FAMILY -> 10751
    MovieGenre.FANTASY -> 14
    MovieGenre.HISTORY -> 36
    MovieGenre.HORROR -> 27
    MovieGenre.MUSIC -> 10402
    MovieGenre.MYSTERY -> 9648
    MovieGenre.ROMANCE -> 10749
    MovieGenre.SC_IFI -> 878
    MovieGenre.TV_MOVIE -> 10770
    MovieGenre.THRILLER -> 53
    MovieGenre.WAR -> 10752
    MovieGenre.WESTERN -> 37
    else -> 0
}

fun TvShowGenre.getId(): Int = when (this) {
    TvShowGenre.ACTION_ADVENTURE -> 10759
    TvShowGenre.ANIMATION -> 16
    TvShowGenre.COMEDY -> 35
    TvShowGenre.CRIME -> 80
    TvShowGenre.DOCUMENTARY -> 99
    TvShowGenre.DRAMA -> 18
    TvShowGenre.FAMILY -> 10751
    TvShowGenre.KIDS -> 10762
    TvShowGenre.MYSTERY -> 9648
    TvShowGenre.NEWS -> 10763
    TvShowGenre.REALITY -> 10764
    TvShowGenre.FANTASY -> 10765
    TvShowGenre.SOAP -> 10766
    TvShowGenre.TALK -> 10767
    TvShowGenre.WAR_POLITICS -> 10768
    TvShowGenre.WESTERN -> 37
    else -> 0
}

fun Genre.toGenreId(): Int = when (this) {
    is MovieGenre -> this.getId()
    is TvShowGenre -> this.getId()
}
