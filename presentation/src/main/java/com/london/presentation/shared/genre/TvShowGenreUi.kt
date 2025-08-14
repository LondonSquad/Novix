package com.london.presentation.shared.genre

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.london.domain.entity.genre.TvShowGenre
import com.london.presentation.R

enum class TvShowGenreUi(
    @StringRes val stringResId: Int,
    @DrawableRes val backgroundResId: Int? = null
) {
    All(stringResId = R.string.all),
    ActionAdventure(
        stringResId = R.string.action_adventure,
        backgroundResId = R.drawable.img_action_and_adventure_background
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
    Kids(stringResId = R.string.kids, backgroundResId = R.drawable.img_kids_background),
    Mystery(
        stringResId = R.string.mystery,
        backgroundResId = R.drawable.img_mystery_background
    ),
    News(stringResId = R.string.news, backgroundResId = R.drawable.img_news_background),
    Reality(
        stringResId = R.string.reality,
        backgroundResId = R.drawable.img_reality_background
    ),
    Fantasy(
        stringResId = R.string.fantasy,
        backgroundResId = R.drawable.img_fantasy_background
    ),
    Soap(stringResId = R.string.soap, backgroundResId = R.drawable.img_soap_background),
    Talk(stringResId = R.string.talk, backgroundResId = R.drawable.img_talk_background),
    WarPolitics(
        stringResId = R.string.war_politics,
        backgroundResId = R.drawable.img_war_background
    ),
    Western(
        stringResId = R.string.western,
        backgroundResId = R.drawable.img_western_background
    ),
    Unknown(stringResId = R.string.unknown)
}

fun TvShowGenre.toUi(): TvShowGenreUi {
    return when (this) {
        TvShowGenre.ALL -> TvShowGenreUi.All
        TvShowGenre.ACTION_ADVENTURE -> TvShowGenreUi.ActionAdventure
        TvShowGenre.ANIMATION -> TvShowGenreUi.Animation
        TvShowGenre.COMEDY -> TvShowGenreUi.Comedy
        TvShowGenre.CRIME -> TvShowGenreUi.Crime
        TvShowGenre.DOCUMENTARY -> TvShowGenreUi.Documentary
        TvShowGenre.DRAMA -> TvShowGenreUi.Drama
        TvShowGenre.FAMILY -> TvShowGenreUi.Family
        TvShowGenre.KIDS -> TvShowGenreUi.Kids
        TvShowGenre.MYSTERY -> TvShowGenreUi.Mystery
        TvShowGenre.NEWS -> TvShowGenreUi.News
        TvShowGenre.REALITY -> TvShowGenreUi.Reality
        TvShowGenre.FANTASY -> TvShowGenreUi.Fantasy
        TvShowGenre.SOAP -> TvShowGenreUi.Soap
        TvShowGenre.TALK -> TvShowGenreUi.Talk
        TvShowGenre.WAR_POLITICS -> TvShowGenreUi.WarPolitics
        TvShowGenre.WESTERN -> TvShowGenreUi.Western
        TvShowGenre.UNKNOWN -> TvShowGenreUi.Unknown
    }
}

fun TvShowGenreUi.toDomain(): TvShowGenre {
    return when (this) {
        TvShowGenreUi.All -> TvShowGenre.ALL
        TvShowGenreUi.ActionAdventure -> TvShowGenre.ACTION_ADVENTURE
        TvShowGenreUi.Animation -> TvShowGenre.ANIMATION
        TvShowGenreUi.Comedy -> TvShowGenre.COMEDY
        TvShowGenreUi.Crime -> TvShowGenre.CRIME
        TvShowGenreUi.Documentary -> TvShowGenre.DOCUMENTARY
        TvShowGenreUi.Drama -> TvShowGenre.DRAMA
        TvShowGenreUi.Family -> TvShowGenre.FAMILY
        TvShowGenreUi.Kids -> TvShowGenre.KIDS
        TvShowGenreUi.Mystery -> TvShowGenre.MYSTERY
        TvShowGenreUi.News -> TvShowGenre.NEWS
        TvShowGenreUi.Reality -> TvShowGenre.REALITY
        TvShowGenreUi.Fantasy -> TvShowGenre.FANTASY
        TvShowGenreUi.Soap -> TvShowGenre.SOAP
        TvShowGenreUi.Talk -> TvShowGenre.TALK
        TvShowGenreUi.WarPolitics -> TvShowGenre.WAR_POLITICS
        TvShowGenreUi.Western -> TvShowGenre.WESTERN
        TvShowGenreUi.Unknown -> TvShowGenre.UNKNOWN
    }
}
