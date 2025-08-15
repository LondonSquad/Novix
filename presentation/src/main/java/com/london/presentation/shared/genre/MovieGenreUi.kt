package com.london.presentation.shared.genre

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.london.domain.entity.genre.MovieGenre
import com.london.presentation.R

enum class MovieGenreUi(
    @StringRes val stringResId: Int,
    @DrawableRes val backgroundResId: Int? = null
) {
    All(stringResId = R.string.all),
    Action(
        stringResId = R.string.action,
        backgroundResId = R.drawable.img_action_background
    ),
    Adventure(
        stringResId = R.string.adventure,
        backgroundResId = R.drawable.img_adventure_background
    ),
    Animation(
        stringResId = R.string.animation,
        backgroundResId = R.drawable.img_animation_background
    ),
    Comedy(
        stringResId = R.string.comedy,
        backgroundResId = R.drawable.img_comedy_background
    ),
    Crime(stringResId = R.string.crime, backgroundResId = R.drawable.img_crime_background),
    Documentary(
        stringResId = R.string.documentary,
        backgroundResId = R.drawable.img_documentary_background
    ),
    Drama(stringResId = R.string.drama, backgroundResId = R.drawable.img_drama_background),
    Family(
        stringResId = R.string.family,
        backgroundResId = R.drawable.img_family_background
    ),
    Fantasy(
        stringResId = R.string.fantasy,
        backgroundResId = R.drawable.img_fantasy_background
    ),
    History(
        stringResId = R.string.history,
        backgroundResId = R.drawable.img_history_background
    ),
    Horror(
        stringResId = R.string.horror,
        backgroundResId = R.drawable.img_horror_background
    ),
    Music(
        stringResId = R.string.music,
        backgroundResId = R.drawable.img_music_background
    ),
    Mystery(
        stringResId = R.string.mystery,
        backgroundResId = R.drawable.img_mystery_background
    ),
    Romance(
        stringResId = R.string.romance,
        backgroundResId = R.drawable.img_romance_background
    ),
    SciFi(
        stringResId = R.string.sci_fi,
        backgroundResId = R.drawable.img_sci_fi_and_fantasy_background
    ),
    TvMovie(
        stringResId = R.string.tv_movie,
        backgroundResId = R.drawable.img_tv_movie_background
    ),
    Thriller(
        stringResId = R.string.thriller,
        backgroundResId = R.drawable.img_thriller_background
    ),
    War(stringResId = R.string.war, backgroundResId = R.drawable.img_war_background),
    Western(
        stringResId = R.string.western,
        backgroundResId = R.drawable.img_western_background
    ),
    Unknown(stringResId = R.string.unknown);

    companion object {

        fun getListWithoutAllAndUnknown(): List<MovieGenreUi> {
            return MovieGenreUi.entries.filter { it != MovieGenreUi.All && it != MovieGenreUi.Unknown }
        }

        fun getListWithoutUnknown(): List<MovieGenreUi> {
            return MovieGenreUi.entries.filter { it != MovieGenreUi.Unknown }
        }
    }

}

fun MovieGenre.toUi(): MovieGenreUi = when (this) {
    MovieGenre.ALL -> MovieGenreUi.All
    MovieGenre.ACTION -> MovieGenreUi.Action
    MovieGenre.ADVENTURE -> MovieGenreUi.Adventure
    MovieGenre.ANIMATION -> MovieGenreUi.Animation
    MovieGenre.COMEDY -> MovieGenreUi.Comedy
    MovieGenre.CRIME -> MovieGenreUi.Crime
    MovieGenre.DOCUMENTARY -> MovieGenreUi.Documentary
    MovieGenre.DRAMA -> MovieGenreUi.Drama
    MovieGenre.FAMILY -> MovieGenreUi.Family
    MovieGenre.FANTASY -> MovieGenreUi.Fantasy
    MovieGenre.HISTORY -> MovieGenreUi.History
    MovieGenre.HORROR -> MovieGenreUi.Horror
    MovieGenre.MUSIC -> MovieGenreUi.Music
    MovieGenre.MYSTERY -> MovieGenreUi.Mystery
    MovieGenre.ROMANCE -> MovieGenreUi.Romance
    MovieGenre.SC_IFI -> MovieGenreUi.SciFi
    MovieGenre.TV_MOVIE -> MovieGenreUi.TvMovie
    MovieGenre.THRILLER -> MovieGenreUi.Thriller
    MovieGenre.WAR -> MovieGenreUi.War
    MovieGenre.WESTERN -> MovieGenreUi.Western
    MovieGenre.UNKNOWN -> MovieGenreUi.Unknown
}

fun MovieGenreUi.toDomain(): MovieGenre = when (this) {
    MovieGenreUi.All -> MovieGenre.ALL
    MovieGenreUi.Action -> MovieGenre.ACTION
    MovieGenreUi.Adventure -> MovieGenre.ADVENTURE
    MovieGenreUi.Animation -> MovieGenre.ANIMATION
    MovieGenreUi.Comedy -> MovieGenre.COMEDY
    MovieGenreUi.Crime -> MovieGenre.CRIME
    MovieGenreUi.Documentary -> MovieGenre.DOCUMENTARY
    MovieGenreUi.Drama -> MovieGenre.DRAMA
    MovieGenreUi.Family -> MovieGenre.FAMILY
    MovieGenreUi.Fantasy -> MovieGenre.FANTASY
    MovieGenreUi.History -> MovieGenre.HISTORY
    MovieGenreUi.Horror -> MovieGenre.HORROR
    MovieGenreUi.Music -> MovieGenre.MUSIC
    MovieGenreUi.Mystery -> MovieGenre.MYSTERY
    MovieGenreUi.Romance -> MovieGenre.ROMANCE
    MovieGenreUi.SciFi -> MovieGenre.SC_IFI
    MovieGenreUi.TvMovie -> MovieGenre.TV_MOVIE
    MovieGenreUi.Thriller -> MovieGenre.THRILLER
    MovieGenreUi.War -> MovieGenre.WAR
    MovieGenreUi.Western -> MovieGenre.WESTERN
    MovieGenreUi.Unknown -> MovieGenre.UNKNOWN
}
